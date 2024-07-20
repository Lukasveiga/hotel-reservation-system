package com.devlukas.hotel.hotel.utils;

import com.devlukas.hotel.hotel.entities.hotel.Convenience;
import com.devlukas.hotel.hotel.entities.hotel.Hotel;
import com.devlukas.hotel.hotel.entities.hotel.HotelAddress;

public class HotelUtils {

    public static Hotel generateHotelEntity() {
        return new Hotel("Test Hotel", "80.826.515/0001-84",
                "(11)2233-5599",
                "hotel_test@email.com",
                "Test Hotel with great accommodations to run junit tests", null);
    }

    public static Hotel generateHotelEntity(HotelAddress address) {
        return new Hotel("Test Hotel", "80.826.515/0001-84",
                "(11)2233-5599",
                "hotel_test@email.com",
                "Test Hotel with great accommodations to run junit tests", address);
    }

    public static HotelAddress generateHotelAddress() {
        return new HotelAddress("Brazil", "Parahyba", "Cuité", "Bairro do Ró", "Rua da Já", "12", "520425-70");
    }

    public static Convenience generateConvenience() {
        var convenience = new Convenience();
        convenience.setDescription("Convenience 1");
        return convenience;
    }
}
