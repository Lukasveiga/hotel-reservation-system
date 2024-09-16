package com.devlukas.administration.repositories;

import com.devlukas.administration.entities.ChainAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChainAdminRepository extends JpaRepository<ChainAdmin, Long> {

    Optional<ChainAdmin> findByCNPJ(String CNPJ);
    Optional<ChainAdmin> findByEmail(String email);
}
