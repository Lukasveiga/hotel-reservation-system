package com.devlukas.hotelreservationsystem.hotel.services.auth;

import com.devlukas.hotelreservationsystem.ServiceTestConfig;
import com.devlukas.hotelreservationsystem.hotel.auth.AuthService;
import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdmin;
import com.devlukas.hotelreservationsystem.hotel.entities.hotelAdmin.HotelAdminPrinciple;
import com.devlukas.hotelreservationsystem.security.JWTProvider;
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

    HotelAdmin hotelAdmin;

    @BeforeEach
    void setUp() {
        hotelAdmin = new HotelAdmin();
        hotelAdmin.setCNPJ("1");
        hotelAdmin.setPassword("12345");
        hotelAdmin.setId(1L);
        hotelAdmin.setRoles("admin");
    }

    @Test
    void testCreateLoginInfoSuccess() {
        // Given
        var hotelPrincipal = new HotelAdminPrinciple(hotelAdmin);
        var authentication = UsernamePasswordAuthenticationToken.authenticated(hotelPrincipal,
                hotelPrincipal.getPassword(), hotelPrincipal.getAuthorities());

        when(this.jwtProvider.generateAccessToken(any(Authentication.class)))
                .thenReturn("token");

        // When
        var loginResultMap = this.authService.createLoginInfo(authentication);

        // Then
        Assertions.assertThat(loginResultMap).isNotNull();
        Assertions.assertThat(loginResultMap.get("token")).isEqualTo("token");
        Assertions.assertThat(loginResultMap.get("hotelAdminInfo")).usingRecursiveAssertion().isEqualTo(hotelAdmin);
    }

}