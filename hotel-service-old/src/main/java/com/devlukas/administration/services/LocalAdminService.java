package com.devlukas.administration.services;

import com.devlukas.administration.entities.LocalAdmin;
import com.devlukas.administration.repositories.ChainAdminRepository;
import com.devlukas.administration.repositories.LocalAdminRepository;
import com.devlukas.system.exceptions.ObjectNotFoundException;
import com.devlukas.system.exceptions.UniqueIdentifierAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalAdminService {

    private final LocalAdminRepository localAdminRepository;

    private final ChainAdminRepository chainAdminRepository;

    private final PasswordEncoder encoder;

    public LocalAdminService(LocalAdminRepository localAdminRepository, ChainAdminRepository chainAdminRepository, PasswordEncoder encoder) {
        this.localAdminRepository = localAdminRepository;
        this.chainAdminRepository = chainAdminRepository;
        this.encoder = encoder;
    }

    @Transactional
    public LocalAdmin save(LocalAdmin localAdmin) {
        var chainAdmin = this.chainAdminRepository.findByCNPJ(localAdmin.getCNPJ())
                .orElseThrow(() -> new ObjectNotFoundException("Chain Admin Account", localAdmin.getCNPJ()));

        this.localAdminRepository.findByEmail(localAdmin.getEmail())
                .ifPresent(a -> {
                    throw new UniqueIdentifierAlreadyExistsException("Email");
                });

        localAdmin.setPassword(this.encoder.encode(localAdmin.getPassword()));
        localAdmin.setRoles("localAdmin");

        localAdmin.setChainAdmin(chainAdmin);
        chainAdmin.addLocalAdmin(localAdmin);

        return this.localAdminRepository.save(localAdmin);
    }

    public LocalAdmin findById(Long id) {
        return this.localAdminRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Local Admin Account", id));
    }
}
