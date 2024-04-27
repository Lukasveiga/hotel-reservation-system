package com.devlukas.hotelreservationsystem.hotel.repositories;

import com.devlukas.hotelreservationsystem.hotel.entities.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndHotelId(Long hotelId, Long id);
}
