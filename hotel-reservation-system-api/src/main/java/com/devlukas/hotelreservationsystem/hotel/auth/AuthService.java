package com.devlukas.hotelreservationsystem.hotel.auth;

import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdminPrinciple;
import com.devlukas.hotelreservationsystem.security.JWTProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JWTProvider jwtProvider;

    public AuthService(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        var principal = (HotelAdminPrinciple) authentication.getPrincipal();
        var hotelAdmin = principal.hotelAdmin();

        var token = this.jwtProvider.generateAccessToken(authentication);
        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("hotelAdminInfo", hotelAdmin);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
