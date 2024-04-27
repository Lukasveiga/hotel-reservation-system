package com.devlukas.hotelreservationsystem.hotel.auth;

import com.devlukas.hotelreservationsystem.system.Result;
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
@RequestMapping("${api.endpoint.base-url}/hotel")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Result> getLoginInfo(Authentication authentication, HttpServletRequest request) {
        LOGGER.debug("Authenticated hotel admin: '{}'", authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Hotel Admin Access Token")
                        .localDateTime(LocalDateTime.now())
                        .data(this.authService.createLoginInfo(authentication))
                        .build()
        );
    }
}
