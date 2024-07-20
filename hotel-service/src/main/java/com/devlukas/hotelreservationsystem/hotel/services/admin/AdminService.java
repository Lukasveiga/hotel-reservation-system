package com.devlukas.hotelreservationsystem.hotel.services.admin;

import com.devlukas.hotelreservationsystem.hotel.entities.admin.Admin;
import com.devlukas.hotelreservationsystem.hotel.repositories.AdminRepository;
import com.devlukas.hotelreservationsystem.system.exceptions.UniqueIdentifierAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder encoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    public Admin save(Admin hotelAdmin) {
        this.adminRepository.findByCNPJ(hotelAdmin.getCNPJ())
                .ifPresent(admin -> {
                    throw new UniqueIdentifierAlreadyExistsException("CNPJ");
        });
        hotelAdmin.setPassword(this.encoder.encode(hotelAdmin.getPassword()));
        hotelAdmin.setRoles("hotelAdmin");
        return this.adminRepository.save(hotelAdmin);
    }
}
