package com.ramonda.allo_backend.service;

import com.ramonda.allo_backend.config.WebClientConfig;
import com.ramonda.allo_backend.dto.response.SettlementSummaryResponse;
import com.ramonda.allo_backend.dto.response.SettlementTransaction;
import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.model.Expense;
import com.ramonda.allo_backend.model.Participant;
import com.ramonda.allo_backend.model.Payment;
import com.ramonda.allo_backend.model.SplitType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SettlementServiceTest {

    private final SettlementService settlementService = new SettlementService();

    @SuppressWarnings("null")
    @Test
    void threeWayTripSettlesInMinimalTransactions() {
        // Classic scenario: A, B, C split a 300 trip equally (100 each).
        // A paid 300. So B owes A 100, C owes A 100.
        // Expect exactly 2 transactions, not 3+ redundant ones.
        Participant a = new Participant("a", "Alice");
        Participant b = new Participant("b", "Bob");
        Participant c = new Participant("c", "Carol");
        BigDecimal amt = new BigDecimal("300.00");

        WebClientConfig webClientConfig = new WebClientConfig();
        BigDecimal chargePct = webClientConfig.serviceChargePercentage();
        BigDecimal serviceChargeAmount = amt
                .multiply(chargePct)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = amt.add(serviceChargeAmount);

        BillGroup group = new BillGroup("g1", "Trip", List.of(a, b, c), Instant.now());
        group.addExpense(new Expense(
                "e1", "g1", "a", amt, chargePct, serviceChargeAmount, totalAmount, 
                "Hotel", "accommodation", SplitType.EQUAL,
                Map.of("a", new BigDecimal("100.00"), "b", new BigDecimal("100.00"), "c", new BigDecimal("100.00")),
                Instant.now()));

        SettlementSummaryResponse result = settlementService.calculateSettlement(group);

        assertThat(result.transactions()).hasSize(2);
        BigDecimal totalSettled = result.transactions().stream()
                .map(SettlementTransaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(totalSettled).isEqualByComparingTo("200.00");
    }

    @Test
    void chainOfDebtsCollapsesToMinimalTransactions() {
        // A owes B 50, B owes C 50 -> net effect: A should just pay C 50 directly,
        // not two separate transactions. This tests the actual optimization value.
        Participant a = new Participant("a", "Alice");
        Participant b = new Participant("b", "Bob");
        Participant c = new Participant("c", "Carol");

        BillGroup group = new BillGroup("g1", "Chain", List.of(a, b, c), Instant.now());
        BigDecimal amt = new BigDecimal("50.00");

        WebClientConfig webClientConfig = new WebClientConfig();
        BigDecimal chargePct = webClientConfig.serviceChargePercentage();
        BigDecimal serviceChargeAmount = amt
                .multiply(chargePct)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = amt.add(serviceChargeAmount);

        // B paid for something A owes 50 for
        group.addExpense(new Expense(
                "e1", "g1", "b", amt, chargePct, serviceChargeAmount, totalAmount, "Lunch", "food",
                SplitType.EXACT, Map.of("a", new BigDecimal("50.00")), Instant.now()));

        // C paid for something B owes 50 for
        group.addExpense(new Expense(
                "e2", "g1", "c", amt, chargePct, serviceChargeAmount, totalAmount, "Snacks", "food",
                SplitType.EXACT, Map.of("b", new BigDecimal("50.00")), Instant.now()));

        SettlementSummaryResponse result = settlementService.calculateSettlement(group);

        // Net balances: a = -50, b = 0 (received 50, owes 50), c = +50
        // Optimal: exactly 1 transaction, A -> C 50.00
        assertThat(result.transactions()).hasSize(1);
        SettlementTransaction tx = result.transactions().get(0);
        assertThat(tx.from()).isEqualTo("a");
        assertThat(tx.to()).isEqualTo("c");
        assertThat(tx.amount()).isEqualByComparingTo("50.00");
    }

    @Test
    void recordedPaymentReducesOutstandingBalance() {
        Participant a = new Participant("a", "Alice");
        Participant b = new Participant("b", "Bob");
        BigDecimal amt = new BigDecimal("100.00");

        WebClientConfig webClientConfig = new WebClientConfig();
        BigDecimal chargePct = webClientConfig.serviceChargePercentage();
        BigDecimal serviceChargeAmount = amt
                .multiply(chargePct)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = amt.add(serviceChargeAmount);

        BillGroup group = new BillGroup("g1", "Duo", List.of(a, b), Instant.now());
        group.addExpense(new Expense(
                "e1", "g1", "a", amt, chargePct, serviceChargeAmount, totalAmount, "Dinner", "food",
                SplitType.EQUAL,
                Map.of("a", new BigDecimal("50.00"), "b", new BigDecimal("50.00")),
                Instant.now()));

        // B already paid back 50 -> fully settled
        group.addPayment(new Payment("p1", "g1", "b", "a", new BigDecimal("50.00"), Instant.now()));

        SettlementSummaryResponse result = settlementService.calculateSettlement(group);

        assertThat(result.transactions()).isEmpty();
    }

    @Test
    void everyoneEvenProducesNoTransactions() {
        Participant a = new Participant("a", "Alice");
        Participant b = new Participant("b", "Bob");
        BillGroup group = new BillGroup("g1", "Even", List.of(a, b), Instant.now());

        SettlementSummaryResponse result = settlementService.calculateSettlement(group);

        assertThat(result.transactions()).isEmpty();
    }
}
