package com.devlukas.hotelreservationsystem.controllers.hotel.converter;

import com.devlukas.hotelreservationsystem.controllers.hotel.dto.HotelResponseBody;
import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HotelToResponse implements Converter<Hotel, HotelResponseBody> {
    @Override
    public HotelResponseBody convert(Hotel source) {
        return new HotelResponseBody(source.getId(),
                source.getName(),
                source.getCNPJ(),
                source.getPhone(),
                source.getEmail(),
                source.getDescription(),
                source.getAddress());
    }
}
