package com.devlukas.hotel.hotel.utils;

import com.devlukas.hotel.hotel.entities.admin.Admin;

public class AdminUtils {

    public static Admin generateHotelAdminEntity() {
        var hotelAdmin = new Admin();
        hotelAdmin.setId(1L);
        hotelAdmin.setCNPJ("15.885.735/0001-17");
        hotelAdmin.setPassword("test12345");
        hotelAdmin.setRoles("hotelAdmin");
        return hotelAdmin;
    }
}
