package com.devlukas.administration.controllers.localAdmin;

import com.devlukas.administration.controllers.localAdmin.converter.LocalAdminEntityToResponse;
import com.devlukas.administration.controllers.localAdmin.converter.LocalAdminRequestToEntity;
import com.devlukas.administration.controllers.localAdmin.dto.LocalAdminRequestBody;
import com.devlukas.administration.services.LocalAdminService;
import com.devlukas.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("${api.endpoint.base-url}/admin/local")
public class LocalAdminController {

    private final LocalAdminService localAdminService;

    private final LocalAdminRequestToEntity localAdminRequestToEntity;

    private final LocalAdminEntityToResponse localAdminEntityToResponse;

    public LocalAdminController(LocalAdminService localAdminService, LocalAdminRequestToEntity localAdminRequestToEntity, LocalAdminEntityToResponse localAdminEntityToResponse) {
        this.localAdminService = localAdminService;
        this.localAdminRequestToEntity = localAdminRequestToEntity;
        this.localAdminEntityToResponse = localAdminEntityToResponse;
    }

    @PostMapping
    public ResponseEntity<Result> saveLocalAdmin(@RequestBody @Validated LocalAdminRequestBody requestBody, HttpServletRequest request) {
        var chainAdminCNPJ = getTokenAttribute("CNPJ");

        var localAdmin = this.localAdminRequestToEntity.convert(requestBody);
        Objects.requireNonNull(localAdmin).setCNPJ(chainAdminCNPJ);

        var savedLocalAdmin = this.localAdminService.save(localAdmin);
        var response = this.localAdminEntityToResponse.convert(savedLocalAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Local Admin account created successfully")
                        .data(response)
                        .build()
        );
    }

    private String getTokenAttribute(String attributeKey) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtAuthenticationToken) authentication).getTokenAttributes().get(attributeKey).toString();
    }
}
