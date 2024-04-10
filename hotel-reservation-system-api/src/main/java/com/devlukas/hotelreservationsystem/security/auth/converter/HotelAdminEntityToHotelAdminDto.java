package com.devlukas.hotelreservationsystem.security.auth.converter;

import com.devlukas.hotelreservationsystem.entities.hotelAdmin.HotelAdmin;
import com.devlukas.hotelreservationsystem.security.auth.dto.HotelAdminDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HotelAdminEntityToHotelAdminDto implements Converter<HotelAdmin, HotelAdminDto> {
    @Override
    public HotelAdminDto convert(HotelAdmin source) {
        return new HotelAdminDto(source.getId(), source.getCNPJ(), source.getRoles());
    }
}
