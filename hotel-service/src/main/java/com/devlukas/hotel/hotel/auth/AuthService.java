package com.devlukas.hotel.hotel.auth;

import com.devlukas.hotel.hotel.entities.admin.AdminPrinciple;
import com.devlukas.hotel.security.JWTProvider;
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
        var principal = (AdminPrinciple) authentication.getPrincipal();
        var hotelAdmin = principal.admin();

        var token = this.jwtProvider.generateAccessToken(authentication);
        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("hotelAdminInfo", hotelAdmin);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
