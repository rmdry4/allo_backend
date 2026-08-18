package com.ramonda.allo_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateGroupRequest(

        @NotBlank(message = "Nama group tidak boleh kosong")
        String name,

        @NotEmpty(message = "Minimal harus ada satu peserta")
        @Size(min = 2, message = "Minimal 2 peserta untuk bisa split expense")
        List<@NotBlank String> participantNames
) {
}
