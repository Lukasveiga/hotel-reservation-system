package com.devlukas.hotelreservationsystem.hotel.auth;

import com.devlukas.hotelreservationsystem.hotel.entities.admin.AdminPrinciple;
import com.devlukas.hotelreservationsystem.hotel.repositories.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HotelAdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public HotelAdminUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String cnpj) throws UsernameNotFoundException {
        var hotelAdmin = this.adminRepository.findByCNPJ(cnpj);
        return hotelAdmin.map(AdminPrinciple::new).orElseThrow(() -> new UsernameNotFoundException("Hotel admin not found"));
    }
}
