package com.devlukas.hotelreservationsystem.system;

import com.devlukas.hotelreservationsystem.services.exceptions.ObjectNotFoundException;
import com.devlukas.hotelreservationsystem.services.exceptions.UniqueIdentifierAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(UniqueIdentifierAlreadyExistsException.class)
    ResponseEntity<Result> handleUniqueIdentifierAlreadyExistsException(UniqueIdentifierAlreadyExistsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message(ex.getMessage())
                        .data(null)
                        .build()
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseEntity<Result> handleObjectNotFoundException(ObjectNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message(ex.getMessage())
                        .data(null)
                        .build()
        );
    }
}
