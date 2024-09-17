package com.devlukas.hotelreservationsystem.repository;

import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelAdminRepository extends JpaRepository<HotelAdmin, Long> {

    Optional<HotelAdmin> findByCnpj(String cnpj);
    Optional<HotelAdmin> findByEmail(String email);
}
