package com.devlukas.hotelreservationsystem.controller.admin.converter;

import com.devlukas.hotelreservationsystem.controller.admin.dto.HotelAdminResponseBody;
import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HotelAdminEntityToResponse implements Converter<HotelAdmin, HotelAdminResponseBody> {
    @Override
    public HotelAdminResponseBody convert(HotelAdmin source) {
        return new HotelAdminResponseBody(
                source.getId(),
                source.getEmail(),
                source.getPhone(),
                source.getCnpj(),
                source.getRoles(),
                source.isActive()
        );
    }
}
