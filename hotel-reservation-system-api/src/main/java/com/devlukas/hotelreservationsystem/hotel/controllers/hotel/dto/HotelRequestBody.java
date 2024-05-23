package com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto;

import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record HotelRequestBody(
        @NotBlank(message = "Cannot be empty or null")
        String name,
        @NotBlank(message = "Cannot be empty or null")
        String phone,
        @NotBlank(message = "Cannot be empty or null")
        @Email(message = "Provide a valid email address")
        String email,
        String description,
        @NotNull(message = "Cannot be null")
        HotelAddress address,
        Set<Convenience> conveniences) {
}
