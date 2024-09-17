package com.devlukas.hotelreservationsystem.controller.admin.converter;

import com.devlukas.hotelreservationsystem.controller.admin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HotelAdminRequestToEntity  implements Converter<HotelAdminRequestBody, HotelAdmin> {

    @Override
    public HotelAdmin convert(HotelAdminRequestBody source) {
        var hotel = new HotelAdmin();
        hotel.setEmail(source.email());
        hotel.setPassword(source.password());
        hotel.setPhone(source.phone());
        hotel.setCnpj(source.cnpj());
        return hotel;
    }
}
