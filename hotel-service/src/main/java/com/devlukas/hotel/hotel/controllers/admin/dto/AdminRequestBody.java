package com.devlukas.hotel.hotel.controllers.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminRequestBody(
        @NotBlank(message = "Cannot be empty or null")
        String CNPJ,
        @NotBlank(message = "Cannot be empty or null")
        @Size(min = 8, message = "Must to be at least 8 characters long")
        String password
) {
}