package com.devlukas.administration.auth;

import com.devlukas.administration.entities.AdminPrinciple;
import com.devlukas.administration.repositories.ChainAdminRepository;
import com.devlukas.administration.repositories.LocalAdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HotelAdminUserDetailsService implements UserDetailsService {

    private final ChainAdminRepository chainAdminRepository;
    private final LocalAdminRepository localAdminRepository;

    public HotelAdminUserDetailsService(ChainAdminRepository chainAdminRepository, LocalAdminRepository localAdminRepository) {
        this.chainAdminRepository = chainAdminRepository;
        this.localAdminRepository = localAdminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var chainAdmin = this.chainAdminRepository.findByEmail(email);

        if (chainAdmin.isPresent()) {
            return new AdminPrinciple(chainAdmin.get());
        }

        var localAdmin = this.localAdminRepository.findByEmail(email);

        if (localAdmin.isPresent()) {
            return new AdminPrinciple(localAdmin.get());
        }

        throw new UsernameNotFoundException("Admin account not found");
    }
}
