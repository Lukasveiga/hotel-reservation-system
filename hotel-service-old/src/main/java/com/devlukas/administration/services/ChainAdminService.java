package com.devlukas.administration.services;

import com.devlukas.administration.entities.ChainAdmin;
import com.devlukas.administration.repositories.ChainAdminRepository;
import com.devlukas.system.exceptions.UniqueIdentifierAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChainAdminService {

    private final ChainAdminRepository chainAdminRepository;

    private final PasswordEncoder encoder;

    public ChainAdminService(ChainAdminRepository chainAdminRepository, PasswordEncoder encoder) {
        this.chainAdminRepository = chainAdminRepository;
        this.encoder = encoder;
    }

    @Transactional
    public ChainAdmin save(ChainAdmin chainAdmin) {
        this.chainAdminRepository.findByCNPJ(chainAdmin.getCNPJ())
                .ifPresent(a -> {
                    throw new UniqueIdentifierAlreadyExistsException("CNPJ");
                });

        this.chainAdminRepository.findByEmail(chainAdmin.getEmail())
                .ifPresent(a -> {
                    throw new UniqueIdentifierAlreadyExistsException("Email");
                });

        chainAdmin.setPassword(this.encoder.encode(chainAdmin.getPassword()));
        chainAdmin.setRoles("chainAdmin");

        return this.chainAdminRepository.save(chainAdmin);
    }
}
