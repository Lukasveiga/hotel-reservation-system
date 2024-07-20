package com.devlukas.hotel.hotel.repositories;

import com.devlukas.hotel.hotel.entities.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndHotelId(Long hotelId, Long id);

    List<Room> findAllByHotelId(Long hotelId);
}
