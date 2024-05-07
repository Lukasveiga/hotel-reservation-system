package com.devlukas.hotelreservationsystem.hotel.controllers.hotelAdmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HotelAdminRequestBody(
        @NotBlank(message = "Cannot be empty or null")
        String CNPJ,
        @NotBlank(message = "Cannot be empty or null")
        @Size(min = 8, message = "Must to be at least 8 characters long")
        String password
) {
}