package com.devlukas.hotelreservationsystem.hotel.repositories;

import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelAdminRepository extends JpaRepository<HotelAdmin, Long> {

    Optional<HotelAdmin> findByCNPJ(String CNPJ);
}
