package com.devlukas.hotelreservationsystem.hotel.utils;

import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;

public class HotelAdminUtils {

    public static HotelAdmin generateHotelAdminEntity() {
        var hotelAdmin = new HotelAdmin();
        hotelAdmin.setId(1L);
        hotelAdmin.setCNPJ("15.885.735/0001-17");
        hotelAdmin.setPassword("test12345");
        hotelAdmin.setRoles("hotelAdmin");
        return hotelAdmin;
    }
}
