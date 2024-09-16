package com.devlukas.administration.controllers.chainAdmin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChainAdminRequestBody(
        @NotBlank(message = "Cannot be empty or null")
        String CNPJ,

        @NotBlank(message = "Cannot be empty or null")
        @Email(message = "Must be a valid email")
        String email,

        @NotBlank(message = "Cannot be empty or null")
        String phone,

        @NotBlank(message = "Cannot be empty or null")
        String password
) {}
