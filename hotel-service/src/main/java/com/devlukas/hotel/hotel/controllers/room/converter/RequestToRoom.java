package com.devlukas.hotel.hotel.controllers.room.converter;

import com.devlukas.hotel.hotel.controllers.room.dto.RoomRequestBody;
import com.devlukas.hotel.hotel.entities.room.Room;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestToRoom implements Converter<RoomRequestBody, Room> {

    @Override
    public Room convert(RoomRequestBody source) {
        var room = new Room();
        room.setName(source.name());
        room.setSize(source.size());
        room.setBedsNumber(source.bedsNumber());
        room.setPrice(source.price());
        room.setMaxGuestsNumber(source.maxGuestsNumber());
        return room;
    }
}
