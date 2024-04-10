package com.devlukas.hotelreservationsystem.controllers.hotel.converter;

import com.devlukas.hotelreservationsystem.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToHotel implements Converter<HotelRequestBody, Hotel> {
    @Override
    public Hotel convert(HotelRequestBody source) {
        return new Hotel(source.name(),
                null,
                source.phone(),
                source.email(),
                source.description(),
                source.address());
    }
}