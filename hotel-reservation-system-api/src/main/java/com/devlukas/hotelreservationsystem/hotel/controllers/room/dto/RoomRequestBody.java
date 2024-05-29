package com.devlukas.hotelreservationsystem.hotel.controllers.room.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomRequestBody(
        @NotBlank(message = "Cannot be empty or null")
        String name,
        @NotNull(message = "Cannot be null")
        @Min(value = 0, message = "Cannot be less than zero")
        Double size,
        @NotNull(message = "Cannot be null")
        @Min(value = 0, message = "Cannot be less than zero")
        Integer bedsNumber,
        @NotNull(message = "Cannot be null")
        @Min(value = 0, message = "Cannot be less than zero")
        Integer price,
        @NotNull(message = "Cannot be null")
        @Min(value = 0, message = "Cannot be less than zero")
        Integer maxGuestsNumber
){
}
