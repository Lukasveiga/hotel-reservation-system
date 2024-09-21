package com.devlukas.hotelreservationsystem.usecases.admin;

import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import com.devlukas.hotelreservationsystem.ports.CnpjValidation;
import com.devlukas.hotelreservationsystem.repository.HotelAdminRepository;
import com.devlukas.hotelreservationsystem.usecases.exceptions.UniqueIdentifierAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateHotelAdmin {

    private final HotelAdminRepository repository;
    private final PasswordEncoder encoder;
    private final CnpjValidation cnpjValidation;

    public CreateHotelAdmin(HotelAdminRepository repository, PasswordEncoder encoder, CnpjValidation cnpjValidation) {
        this.repository = repository;
        this.encoder = encoder;
        this.cnpjValidation = cnpjValidation;
    }

    @Transactional
    public HotelAdmin execute(HotelAdmin hotelAdmin) {
        this.repository.findByCnpj(hotelAdmin.getCnpj())
                .ifPresent(e -> {throw new UniqueIdentifierAlreadyExistsException("cnpj");});

        this.repository.findByEmail(hotelAdmin.getEmail())
                .ifPresent(e -> {throw new UniqueIdentifierAlreadyExistsException("email");});

        if (!this.cnpjValidation.validate(hotelAdmin.getCnpj())) {
            throw new IllegalArgumentException("Invalid CNPJ");
        }

        hotelAdmin.setPassword(this.encoder.encode(hotelAdmin.getPassword()));
        hotelAdmin.setActive(true);
        hotelAdmin.setRoles("admin");

        return this.repository.save(hotelAdmin);
    }
}
