package com.devlukas.hotelreservationsystem.utils;

import com.devlukas.hotelreservationsystem.controller.admin.dto.HotelAdminRequestBody;
import com.devlukas.hotelreservationsystem.domain.HotelAdmin;

public class HotelAdminUtils {

    public static HotelAdmin generateHotelAdmin() {
        var h = new HotelAdmin();
        h.setId(1L);
        h.setEmail("hotel_admin_test@email.com");
        h.setPhone("(55)97777-5555");
        h.setCnpj("72.797.458/0001-24");
        h.setRoles("admin");

        return h;
    }

    public static HotelAdminRequestBody generateHotelAdminRequestBody() {
        return new HotelAdminRequestBody(
                "test@email.com",
                "password",
                "(55)988774422",
                "28.315.742/0001-25"
        );
    }
}
