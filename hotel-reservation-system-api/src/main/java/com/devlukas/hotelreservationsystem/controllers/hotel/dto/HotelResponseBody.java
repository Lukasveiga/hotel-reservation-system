package com.devlukas.hotelreservationsystem.controllers.hotel.dto;

import com.devlukas.hotelreservationsystem.entities.hotel.Assessment;
import com.devlukas.hotelreservationsystem.entities.hotel.Convenience;
import com.devlukas.hotelreservationsystem.entities.hotel.HotelAddress;

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
