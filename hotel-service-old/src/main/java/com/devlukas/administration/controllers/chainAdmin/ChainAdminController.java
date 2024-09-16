package com.devlukas.administration.controllers.chainAdmin;

import com.devlukas.administration.controllers.chainAdmin.converter.ChainAdminEntityToResponse;
import com.devlukas.administration.controllers.chainAdmin.converter.ChainAdminRequestToEntity;
import com.devlukas.administration.controllers.chainAdmin.dto.ChainAdminRequestBody;
import com.devlukas.administration.services.ChainAdminService;
import com.devlukas.system.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("${api.endpoint.base-url}/admin/chain")
public class ChainAdminController {

    private final ChainAdminService chainAdminService;

    private final ChainAdminRequestToEntity chainAdminRequestToEntity;

    private final ChainAdminEntityToResponse chainAdminEntityToResponse;

    public ChainAdminController(ChainAdminService chainAdminService, ChainAdminRequestToEntity chainAdminRequestToEntity, ChainAdminEntityToResponse chainAdminEntityToResponse) {
        this.chainAdminService = chainAdminService;
        this.chainAdminRequestToEntity = chainAdminRequestToEntity;
        this.chainAdminEntityToResponse = chainAdminEntityToResponse;
    }

    @PostMapping
    public ResponseEntity<Result> saveChainAdmin(@RequestBody @Validated ChainAdminRequestBody requestBody, HttpServletRequest request) {
        var chainAdmin = this.chainAdminRequestToEntity.convert(requestBody);
        var savedChainAdmin = this.chainAdminService.save(Objects.requireNonNull(chainAdmin));
        var response = this.chainAdminEntityToResponse.convert(savedChainAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(true)
                        .message("Chain admin account created successfully")
                        .data(response)
                        .build()
        );
    }
}
