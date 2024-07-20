package com.devlukas.hotel.hotel.controllers.room.converter;

import com.devlukas.hotel.hotel.controllers.room.dto.RoomResponseBody;
import com.devlukas.hotel.hotel.entities.room.Room;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoomToResponse implements Converter<Room, RoomResponseBody> {

    @Override
    public RoomResponseBody convert(Room source) {
        return new RoomResponseBody(
                source.getId(),
                source.getName(),
                source.getSize(),
                source.getBedsNumber(),
                source.getPrice(),
                source.getMaxGuestsNumber(),
                source.getSituation().getValue(),
                source.getHotel().getId()
        );
    }
}
