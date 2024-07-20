package com.devlukas.hotel.hotel.utils;

import com.devlukas.hotel.hotel.entities.room.Room;
import com.devlukas.hotel.hotel.entities.room.Situation;

public class RoomUtils {

    public static Room generateRoomEntity() {
        return new Room("Test Room 1", 15.0, 2, 299, 4, Situation.AVAILABLE);
    }
}
