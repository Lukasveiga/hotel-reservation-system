package com.devlukas.hotelreservationsystem.hotel.auth.converter;

import com.devlukas.hotelreservationsystem.hotel.auth.dto.HotelAdminDto;
import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HotelAdminEntityToHotelAdminDto implements Converter<HotelAdmin, HotelAdminDto> {
    @Override
    public HotelAdminDto convert(HotelAdmin source) {
        return new HotelAdminDto(source.getId(), source.getCNPJ(), source.getRoles());
    }
}
