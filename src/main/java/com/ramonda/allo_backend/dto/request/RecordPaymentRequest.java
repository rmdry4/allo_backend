package com.ramonda.allo_backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RecordPaymentRequest(

        @NotBlank(message = "from (participant id) tidak boleh kosong")
        String from,

        @NotBlank(message = "to (participant id) tidak boleh kosong")
        String to,

        @NotNull(message = "amount tidak boleh kosong")
        @DecimalMin(value = "0.01", message = "amount harus lebih besar dari 0")
        BigDecimal amount
) {
}
