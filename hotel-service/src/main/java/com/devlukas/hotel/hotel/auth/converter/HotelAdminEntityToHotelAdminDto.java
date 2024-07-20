package com.devlukas.hotel.hotel.auth.converter;

import com.devlukas.hotel.hotel.auth.dto.HotelAdminDto;
import com.devlukas.hotel.hotel.entities.admin.Admin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HotelAdminEntityToHotelAdminDto implements Converter<Admin, HotelAdminDto> {
    @Override
    public HotelAdminDto convert(Admin source) {
        return new HotelAdminDto(source.getId(), source.getCNPJ(), source.getRoles());
    }
}
