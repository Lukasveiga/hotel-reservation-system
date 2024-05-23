package com.devlukas.hotelreservationsystem.hotel.controllers.admin.converter;

import com.devlukas.hotelreservationsystem.hotel.controllers.admin.dto.AdminRequestBody;
import com.devlukas.hotelreservationsystem.hotel.entities.admin.Admin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToAdmin implements Converter<AdminRequestBody, Admin> {
    @Override
    public Admin convert(AdminRequestBody source) {
        var hotel = new Admin();
        hotel.setCNPJ(source.CNPJ());
        hotel.setPassword(source.password());
        return hotel;
    }
}
