package com.devlukas.hotel.hotel.controllers.hotel.converter;

import com.devlukas.hotel.hotel.controllers.hotel.dto.ConvenienceResponseBody;
import com.devlukas.hotel.hotel.controllers.hotel.dto.HotelResponseBody;
import com.devlukas.hotel.hotel.entities.hotel.Hotel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
                source.getAddress(),
                source.getConveniences().stream()
                        .map(convenience -> new ConvenienceResponseBody(convenience.getId(), convenience.getDescription()))
                        .collect(Collectors.toSet()),
                source.getAssessments());
    }
}
