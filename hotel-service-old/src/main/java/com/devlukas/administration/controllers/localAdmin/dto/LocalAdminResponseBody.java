package com.devlukas.administration.controllers.localAdmin.dto;

public record LocalAdminResponseBody(
        Long id,
        String CNPJ,
        String email,
        String phone,
        Long hotelId,
        String roles
) {}
