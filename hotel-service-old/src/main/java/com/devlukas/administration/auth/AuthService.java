package com.devlukas.administration.auth;

import com.devlukas.administration.entities.AdminPrinciple;
import com.devlukas.security.JWTProvider;
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
        var admin = principal.admin();

        var token = this.jwtProvider.generateAccessToken(authentication);
        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("adminInfo", admin);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
