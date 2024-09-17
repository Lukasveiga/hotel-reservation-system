package com.devlukas.hotelreservationsystem.controller.admin.dto;


public record HotelAdminResponseBody(
        Long id,
        String email,
        String phone,
        String cnpj,
        String roles,
        boolean isActive
) {
}
