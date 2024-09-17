package com.devlukas.hotelreservationsystem.usecases.hotelAdmin;

import com.devlukas.hotelreservationsystem.domain.HotelAdmin;
import com.devlukas.hotelreservationsystem.repository.HotelAdminRepository;
import com.devlukas.hotelreservationsystem.usecases.exceptions.UniqueIdentifierAlreadyExists;
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
                .ifPresent(e -> {throw new UniqueIdentifierAlreadyExists("cnpj");});

        this.repository.findByEmail(hotelAdmin.getEmail())
                .ifPresent(e -> {throw new UniqueIdentifierAlreadyExists("email");});

        hotelAdmin.setPassword(this.encoder.encode(hotelAdmin.getPassword()));
        hotelAdmin.setRoles("admin");

        return this.repository.save(hotelAdmin);
    }
}
