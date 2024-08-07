package com.devlukas.hotel.hotel.auth;

import com.devlukas.hotel.hotel.auth.converter.HotelAdminEntityToHotelAdminDto;
import com.devlukas.hotel.hotel.entities.admin.Admin;
import com.devlukas.hotel.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.endpoint.base-url}/hotel/login")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    private final HotelAdminEntityToHotelAdminDto hotelAdminEntityToHotelAdminDto;

    public AuthController(AuthService authService, HotelAdminEntityToHotelAdminDto hotelAdminEntityToHotelAdminDto) {
        this.authService = authService;
        this.hotelAdminEntityToHotelAdminDto = hotelAdminEntityToHotelAdminDto;
    }

    @PostMapping()
    public ResponseEntity<Result> getLoginInfo(Authentication authentication, HttpServletRequest request) {
        LOGGER.debug("Authenticated hotel admin: '{}'", authentication.getName());

        var loginResult = this.authService.createLoginInfo(authentication);
        loginResult.replace("hotelAdminInfo",
                this.hotelAdminEntityToHotelAdminDto.convert((Admin) loginResult.get("hotelAdminInfo")));

        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Hotel Admin Access Token")
                        .localDateTime(LocalDateTime.now())
                        .data(loginResult)
                        .build()
        );
    }
}
