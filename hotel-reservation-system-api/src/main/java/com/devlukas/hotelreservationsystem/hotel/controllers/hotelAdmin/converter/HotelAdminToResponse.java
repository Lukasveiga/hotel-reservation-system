package com.devlukas.hotelreservationsystem.hotel.controllers.hotelAdmin.converter;

import com.devlukas.hotelreservationsystem.hotel.controllers.hotelAdmin.dto.HotelAdminResponseBody;
import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HotelAdminToResponse implements Converter<HotelAdmin, HotelAdminResponseBody> {
    @Override
    public HotelAdminResponseBody convert(HotelAdmin source) {
        return new HotelAdminResponseBody(source.getId(), source.getCNPJ(), source.getRoles());
    }
}