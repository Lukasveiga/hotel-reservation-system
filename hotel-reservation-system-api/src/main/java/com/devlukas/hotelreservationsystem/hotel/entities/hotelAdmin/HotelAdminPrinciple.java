package com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class HotelAdminPrinciple implements UserDetails {

    private final HotelAdmin hotelAdmin;

    public HotelAdminPrinciple(HotelAdmin hotelAdmin) {
        this.hotelAdmin = hotelAdmin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(this.hotelAdmin.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.hotelAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return this.hotelAdmin.getCNPJ();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public HotelAdmin getHotelAdmin() {
        return hotelAdmin;
    }
}
