package com.devlukas.hotel.hotel.controllers.room.dto;

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
