package com.devlukas.hotel.hotel.services.auth;

import com.devlukas.hotel.ServiceTestConfig;
import com.devlukas.hotel.hotel.auth.AuthService;
import com.devlukas.hotel.hotel.entities.admin.Admin;
import com.devlukas.hotel.hotel.entities.admin.AdminPrinciple;
import com.devlukas.hotel.security.JWTProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest implements ServiceTestConfig {

    @Mock
    JWTProvider jwtProvider;

    @InjectMocks
    AuthService authService;

    Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setCNPJ("1");
        admin.setPassword("12345");
        admin.setId(1L);
        admin.setRoles("admin");
    }

    @Test
    void testCreateLoginInfoSuccess() {
        // Given
        var hotelPrincipal = new AdminPrinciple(admin);
        var authentication = UsernamePasswordAuthenticationToken.authenticated(hotelPrincipal,
                hotelPrincipal.getPassword(), hotelPrincipal.getAuthorities());

        when(this.jwtProvider.generateAccessToken(any(Authentication.class)))
                .thenReturn("token");

        // When
        var loginResultMap = this.authService.createLoginInfo(authentication);

        // Then
        Assertions.assertThat(loginResultMap).isNotNull();
        Assertions.assertThat(loginResultMap.get("token")).isEqualTo("token");
        Assertions.assertThat(loginResultMap.get("hotelAdminInfo")).usingRecursiveAssertion().isEqualTo(admin);
    }

}