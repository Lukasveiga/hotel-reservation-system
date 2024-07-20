package com.devlukas.hotel.hotel.controllers.hotel.converter;

import com.devlukas.hotel.hotel.controllers.hotel.dto.HotelRequestBody;
import com.devlukas.hotel.hotel.entities.hotel.Hotel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToHotel implements Converter<HotelRequestBody, Hotel> {
    @Override
    public Hotel convert(HotelRequestBody source) {
        Hotel hotel = new Hotel(source.name(),
                null,
                source.phone(),
                source.email(),
                source.description(),
                source.address());
        source.conveniences().forEach(hotel::addConveniences);
        return hotel;
    }
}