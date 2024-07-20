package com.devlukas.hotelreservationsystem.hotel.controllers.admin.converter;

import com.devlukas.hotelreservationsystem.hotel.controllers.admin.dto.AdminResponseBody;
import com.devlukas.hotelreservationsystem.hotel.entities.admin.Admin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AdminToResponse implements Converter<Admin, AdminResponseBody> {
    @Override
    public AdminResponseBody convert(Admin source) {
        return new AdminResponseBody(source.getId(), source.getCNPJ(), source.getRoles());
    }
}
