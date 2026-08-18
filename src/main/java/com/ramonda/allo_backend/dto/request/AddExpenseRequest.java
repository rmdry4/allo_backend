package com.ramonda.allo_backend.dto.request;

import com.ramonda.allo_backend.model.SplitType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record AddExpenseRequest(

        @NotBlank(message = "paidBy (participant id) tidak boleh kosong")
        String paidBy,

        @NotNull(message = "amount tidak boleh kosong")
        @DecimalMin(value = "0.01", message = "amount harus lebih besar dari 0")
        BigDecimal amount,

        @NotBlank(message = "description tidak boleh kosong")
        String description,

        String category, // optional: food, transport, accommodation, etc.

        @NotNull(message = "splitType tidak boleh kosong")
        SplitType splitType,

        @NotEmpty(message = "splitAmong (daftar participant id yang menanggung) tidak boleh kosong")
        List<String> splitAmong,

        /**
         * Diperlukan untuk PERCENTAGE (participantId -> persentase) dan
         * EXACT (participantId -> nominal). Diabaikan untuk EQUAL.
         */
        Map<String, BigDecimal> splitInput,

        @DecimalMin(value = "0", message = "service charge tidak boleh negatif")
        @DecimalMax(value = "100", message = "service charge tidak boleh lebih dari 100%")
        BigDecimal serviceChargePercentage
) {
}
