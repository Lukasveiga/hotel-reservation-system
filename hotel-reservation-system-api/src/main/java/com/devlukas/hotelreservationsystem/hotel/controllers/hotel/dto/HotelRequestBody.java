package com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto;

import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;

import java.util.Set;

public record HotelRequestBody(String name,
                               String phone,
                               String email,
                               String description,
                               HotelAddress address,
                               Set<Convenience> conveniences) {
}
