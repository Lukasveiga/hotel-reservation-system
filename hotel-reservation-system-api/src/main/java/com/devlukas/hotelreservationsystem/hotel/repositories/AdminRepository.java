package com.devlukas.hotelreservationsystem.hotel.repositories;

import com.devlukas.hotelreservationsystem.hotel.entities.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByCNPJ(String CNPJ);
}
