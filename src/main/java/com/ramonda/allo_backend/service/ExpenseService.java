package com.ramonda.allo_backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ramonda.allo_backend.dto.request.AddExpenseRequest;
import com.ramonda.allo_backend.exception.InvalidSplitException;
import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.model.Expense;
import com.ramonda.allo_backend.repository.BillGroupRepository;
import com.ramonda.allo_backend.split.SplitStrategy;
import com.ramonda.allo_backend.split.SplitStrategyFactory;
import com.ramonda.allo_backend.config.WebClientConfig;

@Service
public class ExpenseService {
    private final BillGroupRepository repository;
    private final BillGroupService billGroupService;
    private final SplitStrategyFactory splitStrategyFactory;

    public ExpenseService(BillGroupRepository repository,
                           BillGroupService billGroupService,
                           SplitStrategyFactory splitStrategyFactory) {
        this.repository = repository;
        this.billGroupService = billGroupService;
        this.splitStrategyFactory = splitStrategyFactory;
    }

    // input data pengeluaran tiap-tiap partisipan berdasarkan id group
    // data berupa id group, id partisipan, nominal, deskripsi pengeluaran, kategori, dan dibayar oleh siapa
    public Expense addExpense(String groupId, AddExpenseRequest request) {
        BillGroup group = billGroupService.getGroup(groupId);

        validateParticipant(group, request.paidBy(), "paidBy");
        request.splitAmong().forEach(pid -> validateParticipant(group, pid, "splitAmong"));

        // Split strategy hitung porsi dari SUBTOTAL
        SplitStrategy strategy = splitStrategyFactory.get(request.splitType());
        Map<String, BigDecimal> rawSplit = strategy.calculateSplit(
            request.amount(), request.splitAmong(), request.splitInput());

        // Hitung service charge
        WebClientConfig webClientConfig = new WebClientConfig();
        BigDecimal chargePct = webClientConfig.serviceChargePercentage();
        BigDecimal serviceChargeAmount = request.amount()
                .multiply(chargePct)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = request.amount().add(serviceChargeAmount);
        
        // Distribusikan charge secara proporsional ke tiap porsi mentah
        Map<String, BigDecimal> finalSplit = applyServiceChargeProportionally(
                rawSplit, request.amount(), serviceChargeAmount);

        Expense expense = new Expense(
                UUID.randomUUID().toString(), groupId, request.paidBy(),
                request.amount(), chargePct, serviceChargeAmount, totalAmount,
                request.description(), request.category(), request.splitType(),
                finalSplit, Instant.now());

            repository.addExpense(groupId, expense);
            return expense;
        }

    // validasi input partisipan dengan list partisipan berdasar data group
    private void validateParticipant(BillGroup group, String participantId, String field) {
        if (!group.hasParticipant(participantId)) {
            throw new InvalidSplitException(
                    "'" + field + "' berisi participant id yang tidak terdaftar di group: " + participantId);
        }
    }

    private Map<String, BigDecimal> applyServiceChargeProportionally(
        Map<String, BigDecimal> rawSplit, BigDecimal subtotal, BigDecimal serviceChargeAmount) {

        if (serviceChargeAmount.compareTo(BigDecimal.ZERO) == 0) {
            return rawSplit; // tidak ada charge, tidak perlu proses tambahan
        }

        Map<String, BigDecimal> result = new LinkedHashMap<>();
        BigDecimal distributed = BigDecimal.ZERO;
        List<String> keys = new ArrayList<>(rawSplit.keySet());

        for (int i = 0; i < keys.size(); i++) {
            String pid = keys.get(i);
            BigDecimal raw = rawSplit.get(pid);
            BigDecimal finalAmount;

            if (i == keys.size() - 1) {
                // peserta terakhir menyerap sisa pembulatan, sama seperti pola di strategy lain
                finalAmount = subtotal.add(serviceChargeAmount).subtract(distributed);
            } else {
                BigDecimal proportion = raw.divide(subtotal, 10, RoundingMode.HALF_UP);
                BigDecimal chargeShare = serviceChargeAmount.multiply(proportion)
                        .setScale(2, RoundingMode.HALF_UP);
                finalAmount = raw.add(chargeShare);
                distributed = distributed.add(finalAmount);
            }
            result.put(pid, finalAmount);
        }
        return result;
    }
}
