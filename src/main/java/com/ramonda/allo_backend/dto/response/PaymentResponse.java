package com.ramonda.allo_backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.ramonda.allo_backend.model.Payment;

public record PaymentResponse(String id, String groupId, String from, String to,
                               BigDecimal amount, Instant paidAt) {

    public static PaymentResponse from(Payment p) {
        return new PaymentResponse(p.getId(), p.getGroupId(), p.getFrom(), p.getTo(), p.getAmount(), p.getPaidAt());
    }
}
