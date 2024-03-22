package com.devlukas.hotelreservationsystem.controllers.system;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Result(
        String path,
        Boolean flag,
        String message,
        LocalDateTime localDateTime,
        Object data) {

}
