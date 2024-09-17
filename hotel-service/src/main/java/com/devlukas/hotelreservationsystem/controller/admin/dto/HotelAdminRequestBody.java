package com.devlukas.hotelreservationsystem.controller.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record HotelAdminRequestBody(
        @NotBlank(message = "Cannot be empty or null")
        String email,
        @NotBlank(message = "Cannot be empty or null")
        String password,
        @NotBlank(message = "Cannot be empty or null")
        String phone,
        @NotBlank(message = "Cannot be empty or null")
        String cnpj
) {
}
