package com.devlukas.hotelreservationsystem.hotel.controllers.room.dto;

public record RoomResponseBody(
        Long id,
        String name,
        Double size,
        Integer bedsNumber,
        Integer price,
        Integer maxGuestsNumber,
        String situation,
        Long hotelId
) {
}
