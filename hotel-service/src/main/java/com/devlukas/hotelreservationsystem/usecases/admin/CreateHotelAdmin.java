package com.devlukas.hotelreservationsystem.usecases.admin;

import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import com.devlukas.hotelreservationsystem.repository.HotelAdminRepository;
import com.devlukas.hotelreservationsystem.usecases.exceptions.UniqueIdentifierAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateHotelAdmin {

    private final HotelAdminRepository repository;
    private final PasswordEncoder encoder;

    public CreateHotelAdmin(HotelAdminRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public HotelAdmin execute(HotelAdmin hotelAdmin) {
        this.repository.findByCnpj(hotelAdmin.getCnpj())
                .ifPresent(e -> {throw new UniqueIdentifierAlreadyExistsException("cnpj");});

        this.repository.findByEmail(hotelAdmin.getEmail())
                .ifPresent(e -> {throw new UniqueIdentifierAlreadyExistsException("email");});

        hotelAdmin.setPassword(this.encoder.encode(hotelAdmin.getPassword()));
        hotelAdmin.setActive(true);
        hotelAdmin.setRoles("admin");

        return this.repository.save(hotelAdmin);
    }
}
