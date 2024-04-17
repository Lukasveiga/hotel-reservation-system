package com.devlukas.hotelreservationsystem.controllers.hotel.dto;

import com.devlukas.hotelreservationsystem.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.entities.hotel.HotelAddress;

import java.util.Set;

public record HotelRequestBody(String name,
                               String phone,
                               String email,
                               String description,
                               HotelAddress address,
                               Set<Convenience> conveniences) {
}
