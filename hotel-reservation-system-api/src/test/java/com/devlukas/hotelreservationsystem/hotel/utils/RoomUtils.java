package com.devlukas.hotelreservationsystem.hotel.utils;

import com.devlukas.hotelreservationsystem.hotel.entities.room.Room;
import com.devlukas.hotelreservationsystem.hotel.entities.room.Situation;

public class RoomUtils {

    public static Room generateRoomEntity() {
        return new Room("Test Room 1", 15.0, 2, 299, 4, Situation.AVAILABLE);
    }
}
