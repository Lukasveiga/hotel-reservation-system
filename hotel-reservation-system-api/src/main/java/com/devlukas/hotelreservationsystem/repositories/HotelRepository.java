package com.devlukas.hotelreservationsystem.repositories;

import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByCNPJ(String CNPJ);
}
