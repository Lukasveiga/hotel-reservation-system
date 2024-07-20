package com.devlukas.hotelreservationsystem.hotel.controllers.hotel.dto;

import com.devlukas.hotelreservationsystem.hotel.entities.hotel.Assessment;
import com.devlukas.hotelreservationsystem.hotel.entities.hotel.HotelAddress;

import java.util.Set;

public record HotelResponseBody(Long id,
                                String name,
                                String CNPJ,
                                String phone,
                                String email,
                                String description,
                                HotelAddress address,
                                Set<ConvenienceResponseBody> conveniences,
                                Set<Assessment> assessments) {
}
