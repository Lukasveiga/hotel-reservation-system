package com.devlukas.hotelreservationsystem.controllers.hotel.dto;

import com.devlukas.hotelreservationsystem.entities.hotel.HotelAddress;

public record HotelRequestBody(String name, String CNPJ, String phone, String email, String description, HotelAddress address) {
}
