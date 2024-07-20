package com.devlukas.hotel.hotel.repositories;

import com.devlukas.hotel.hotel.entities.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByCNPJ(String CNPJ);
}
