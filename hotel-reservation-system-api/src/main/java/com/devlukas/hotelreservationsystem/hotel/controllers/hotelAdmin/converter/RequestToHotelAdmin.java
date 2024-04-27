package com.devlukas.hotelreservationsystem.hotel.controllers.hotelAdmin.converter;

import com.devlukas.hotelreservationsystem.hotel.controllers.hotelAdmin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToHotelAdmin implements Converter<HotelAdminRequestBody, HotelAdmin> {
    @Override
    public HotelAdmin convert(HotelAdminRequestBody source) {
        var hotel = new HotelAdmin();
        hotel.setCNPJ(source.CNPJ());
        hotel.setPassword(source.password());
        hotel.setRoles(source.roles());
        return hotel;
    }
}
