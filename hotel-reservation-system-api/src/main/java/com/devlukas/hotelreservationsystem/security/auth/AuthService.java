package com.devlukas.hotelreservationsystem.security.auth;

import com.devlukas.hotelreservationsystem.entities.hotelAdmin.HotelAdminPrinciple;
import com.devlukas.hotelreservationsystem.security.JWTProvider;
import com.devlukas.hotelreservationsystem.security.auth.converter.HotelAdminEntityToHotelAdminDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JWTProvider jwtProvider;

    private final HotelAdminEntityToHotelAdminDto hotelAdminEntityToHotelAdminDto;

    public AuthService(JWTProvider jwtProvider, HotelAdminEntityToHotelAdminDto hotelAdminEntityToHotelAdminDto) {
        this.jwtProvider = jwtProvider;
        this.hotelAdminEntityToHotelAdminDto = hotelAdminEntityToHotelAdminDto;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        var principal = (HotelAdminPrinciple) authentication.getPrincipal();
        var hotelAdmin = principal.getHotelAdmin();

        var token = this.jwtProvider.generateAccessToken(authentication);
        Map<String, Object> loginResultMap = new HashMap<>();

        var hotelAdminDto = this.hotelAdminEntityToHotelAdminDto.convert(hotelAdmin);

        loginResultMap.put("hotelAdminInfo", hotelAdminDto);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
