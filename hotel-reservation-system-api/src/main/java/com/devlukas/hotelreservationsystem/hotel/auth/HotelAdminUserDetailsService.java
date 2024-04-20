package com.devlukas.hotelreservationsystem.hotel.auth;

import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;
import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdminPrinciple;
import com.devlukas.hotelreservationsystem.hotel.repositories.HotelAdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HotelAdminUserDetailsService implements UserDetailsService {

    private final HotelAdminRepository hotelAdminRepository;

    public HotelAdminUserDetailsService(HotelAdminRepository hotelAdminRepository) {
        this.hotelAdminRepository = hotelAdminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String cnpj) throws UsernameNotFoundException {
        Optional<HotelAdmin> hotelAdmin = this.hotelAdminRepository.findByCNPJ(cnpj);
        return hotelAdmin.map(HotelAdminPrinciple::new).orElseThrow(() -> new UsernameNotFoundException("Hotel admin not found"));
    }
}
