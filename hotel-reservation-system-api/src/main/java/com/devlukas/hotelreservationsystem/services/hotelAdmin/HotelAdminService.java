package com.devlukas.hotelreservationsystem.services.hotelAdmin;

import com.devlukas.hotelreservationsystem.entities.hotelAdmin.HotelAdmin;
import com.devlukas.hotelreservationsystem.repositories.HotelAdminRepository;
import com.devlukas.hotelreservationsystem.services.exceptions.UniqueIdentifierAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HotelAdminService {

    private final HotelAdminRepository hotelAdminRepository;

    private final PasswordEncoder encoder;

    public HotelAdminService(HotelAdminRepository hotelAdminRepository, PasswordEncoder encoder) {
        this.hotelAdminRepository = hotelAdminRepository;
        this.encoder = encoder;
    }

    public HotelAdmin save(HotelAdmin hotelAdmin) {
        this.hotelAdminRepository.findByCNPJ(hotelAdmin.getCNPJ())
                .ifPresent(admin -> {
                    throw new UniqueIdentifierAlreadyExistsException("CNPJ");
        });
        hotelAdmin.setPassword(this.encoder.encode(hotelAdmin.getPassword()));
        return this.hotelAdminRepository.save(hotelAdmin);
    }
}
