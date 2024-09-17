package com.devlukas.hotelreservationsystem.config.api;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResultBody(
        String path,
        boolean flag,
        String message,
        LocalDateTime dateTime,
        Object data
) {
}
