package com.devlukas.hotelreservationsystem.config.api;

import com.devlukas.hotelreservationsystem.usecases.exceptions.UniqueIdentifierAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(UniqueIdentifierAlreadyExistsException.class)
    ResponseEntity<ResultBody> handlerUniqueIdentifierAlreadyExistsException(UniqueIdentifierAlreadyExistsException ex,
                                                                             HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ResultBody.builder()
                                .path(request.getRequestURI())
                                .flag(false)
                                .dateTime(LocalDateTime.now())
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    ResponseEntity<ResultBody> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ResultBody.builder()
                                .path(request.getRequestURI())
                                .flag(false)
                                .dateTime(LocalDateTime.now())
                                .message("API endpoint not found")
                                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ResultBody> handleOthersExceptions(Exception ex, HttpServletRequest request) {
        LOGGER.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ResultBody.builder()
                                .path(request.getRequestURI())
                                .flag(false)
                                .dateTime(LocalDateTime.now())
                                .message("Internal Server Error")
                                .build()
        );
    }
}
