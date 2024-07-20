package com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto;

import jakarta.validation.constraints.NotBlank;

public record ConvenienceRequestBody(
        @NotBlank(message = "Cannot be empty or null")
        String description
) {
}
