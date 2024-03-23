package com.devlukas.hotelreservationsystem.services.room;

import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import com.devlukas.hotelreservationsystem.entities.room.Room;
import com.devlukas.hotelreservationsystem.repositories.HotelRepository;
import com.devlukas.hotelreservationsystem.repositories.RoomRepository;
import com.devlukas.hotelreservationsystem.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public Room save(Long hotelId, Room room) {
        var hotel = this.findHotelById(hotelId);
        room.setHotel(hotel);
        hotel.addRoom(room);
        return this.roomRepository.save(room);
    }

    public List<Room> findAll(Long hotelId) {
        var hotel = this.findHotelById(hotelId);
        return hotel.getRooms();
    }

    public Room findById(Long hotelId, Long roomId) {
        var hotel = this.findHotelById(hotelId);
        return this.roomRepository
                .findByIdAndHotelId(hotel.getId(), roomId)
                .orElseThrow(() -> new ObjectNotFoundException("Room", roomId));
    }

    @Transactional
    public Room update(Long hotelId, Long roomId, Room updatedRoom) {
        var oldRoom = this.findById(hotelId, roomId);
        oldRoom.setName(updatedRoom.getName());
        oldRoom.setSize(updatedRoom.getSize());
        oldRoom.setBedsNumber(updatedRoom.getBedsNumber());
        oldRoom.setPrice(updatedRoom.getPrice());
        oldRoom.setMaxGuestsNumber(updatedRoom.getMaxGuestsNumber());
        return this.roomRepository.save(oldRoom);
    }

    @Transactional
    public void delete(Long hotelId, Long roomId) {
        this.roomRepository.delete(this.findById(hotelId, roomId));
    }

    private Hotel findHotelById(Long hotelId) {
        return this.hotelRepository.findById(hotelId).orElseThrow(() -> new ObjectNotFoundException("Hotel", hotelId));
    }
}
