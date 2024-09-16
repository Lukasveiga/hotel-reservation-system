package com.devlukas.administration.repositories;

import com.devlukas.administration.entities.LocalAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalAdminRepository extends JpaRepository<LocalAdmin, Long> {

    Optional<LocalAdmin> findByEmail(String email);
}
