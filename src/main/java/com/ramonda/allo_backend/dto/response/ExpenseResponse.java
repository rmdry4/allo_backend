package com.ramonda.allo_backend.dto.response;

import com.ramonda.allo_backend.model.Expense;
import com.ramonda.allo_backend.model.SplitType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record ExpenseResponse(String id, String groupId, String paidBy, BigDecimal amount,
                               String description, String category, SplitType splitType,
                               Map<String, BigDecimal> splitDetail, Instant createdAt) {

    public static ExpenseResponse from(Expense e) {
        return new ExpenseResponse(e.getId(), e.getGroupId(), e.getPaidBy(), e.getAmount(),
                e.getDescription(), e.getCategory(), e.getSplitType(), e.getSplitDetail(), e.getCreatedAt());
    }
}
