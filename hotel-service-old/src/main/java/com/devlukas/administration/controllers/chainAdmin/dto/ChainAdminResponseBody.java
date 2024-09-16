package com.devlukas.administration.controllers.chainAdmin.dto;

import java.util.List;

public record ChainAdminResponseBody(
        Long id,
        String CNPJ,
        String email,
        String phone,
        List<Long> localAdmins,
        String roles
) {}
