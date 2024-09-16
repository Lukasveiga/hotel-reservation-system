package com.devlukas.administration.auth;

import com.devlukas.administration.entities.Admin;
import com.devlukas.administration.auth.converter.AdminEntityToResponse;
import com.devlukas.system.Result;
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
@RequestMapping("${api.endpoint.base-url}/admin/login")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    private final AdminEntityToResponse adminEntityToResponse;

    public AuthController(AuthService authService, AdminEntityToResponse adminEntityToResponse) {
        this.authService = authService;
        this.adminEntityToResponse = adminEntityToResponse;
    }

    @PostMapping()
    public ResponseEntity<Result> getLoginInfo(Authentication authentication, HttpServletRequest request) {
        LOGGER.debug("Authenticated hotel admin: '{}'. Admin roles: {}", authentication.getName(), authentication.getAuthorities());

        var loginResult = this.authService.createLoginInfo(authentication);
        loginResult.replace("adminInfo",
                this.adminEntityToResponse.convert((Admin) loginResult.get("adminInfo")));

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
