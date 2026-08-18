package com.ramonda.allo_backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.springframework.stereotype.Service;

import com.ramonda.allo_backend.dto.response.SettlementSummaryResponse;
import com.ramonda.allo_backend.dto.response.SettlementTransaction;
import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.model.Expense;
import com.ramonda.allo_backend.model.Participant;
import com.ramonda.allo_backend.model.Payment;

@Service
public class SettlementService {
    // menghitung tagihan
    public SettlementSummaryResponse calculateSettlement(BillGroup group) {
        Map<String, BigDecimal> netBalances = calculateNetBalances(group);
        List<SettlementTransaction> transactions = minimizeTransactions(netBalances);
        return new SettlementSummaryResponse(group.getId(), netBalances, transactions);
    }

    // menghitung saldo tiap-tiap partisipan
    @SuppressWarnings("null")
    private Map<String, BigDecimal> calculateNetBalances(BillGroup group) {
        Map<String, BigDecimal> balances = new HashMap<>();
        for (Participant p : group.getParticipants()) {
            balances.put(p.id(), BigDecimal.ZERO);
        }

        for (Expense expense : group.getExpenses()) {
            balances.merge(expense.getPaidBy(), expense.getAmount(), BigDecimal::add);
            expense.getSplitDetail().forEach((participantId, share) ->
                    balances.merge(participantId, share.negate(), BigDecimal::add));
        }

        for (Payment payment : group.getPayments()) {
            // Paying down a debt increases the payer's balance (less owed)
            // and decreases the receiver's balance (less owed to them).
            balances.merge(payment.getFrom(), payment.getAmount(), BigDecimal::add);
            balances.merge(payment.getTo(), payment.getAmount().negate(), BigDecimal::add);
        }

        return balances;
    }

    private List<SettlementTransaction> minimizeTransactions(Map<String, BigDecimal> netBalances) {
        BigDecimal epsilon = new BigDecimal("0.01");

        PriorityQueue<Map.Entry<String, BigDecimal>> creditors =
                new PriorityQueue<>((a, b) -> b.getValue().compareTo(a.getValue()));
        PriorityQueue<Map.Entry<String, BigDecimal>> debtors =
                new PriorityQueue<>((a, b) -> a.getValue().compareTo(b.getValue()));

        for (Map.Entry<String, BigDecimal> entry : netBalances.entrySet()) {
            BigDecimal balance = entry.getValue();
            if (balance.compareTo(epsilon) > 0) {
                creditors.add(entry);
            } else if (balance.compareTo(epsilon.negate()) < 0) {
                debtors.add(entry);
            }
        }

        List<SettlementTransaction> transactions = new ArrayList<>();

        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            Map.Entry<String, BigDecimal> creditor = creditors.poll();
            Map.Entry<String, BigDecimal> debtor = debtors.poll();

            BigDecimal owed = debtor.getValue().negate();
            BigDecimal receivable = creditor.getValue();
            BigDecimal settleAmount = owed.min(receivable);

            transactions.add(new SettlementTransaction(debtor.getKey(), creditor.getKey(), settleAmount));

            BigDecimal remainingCredit = receivable.subtract(settleAmount);
            BigDecimal remainingDebt = owed.subtract(settleAmount);

            if (remainingCredit.compareTo(epsilon) > 0) {
                creditors.add(Map.entry(creditor.getKey(), remainingCredit));
            }
            if (remainingDebt.compareTo(epsilon) > 0) {
                debtors.add(Map.entry(debtor.getKey(), remainingDebt.negate()));
            }
        }

        return transactions;
    }
}
