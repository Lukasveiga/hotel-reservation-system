package com.devlukas.hotelreservationsystem.repositories;

import com.devlukas.hotelreservationsystem.entities.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndHotelId(Long hotelId, Long id);
}
