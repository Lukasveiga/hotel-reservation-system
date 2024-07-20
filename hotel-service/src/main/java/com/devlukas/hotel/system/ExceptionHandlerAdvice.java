package com.devlukas.hotel.system;

import com.devlukas.hotel.system.exceptions.ObjectNotFoundException;
import com.devlukas.hotel.system.exceptions.UniqueIdentifierAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(UniqueIdentifierAlreadyExistsException.class)
    ResponseEntity<Result> handleUniqueIdentifierAlreadyExistsException(UniqueIdentifierAlreadyExistsException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message(ex.getMessage())
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
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Result> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Result> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());

        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message("Provided arguments are invalid, see data for details")
                        .data(map)
                        .build()

        );
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    ResponseEntity<Result> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message("Incorrect credentials")
                        .build()
        );
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    ResponseEntity<Result> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message("Login credentials are missing")
                        .build()
        );
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    ResponseEntity<Result> handleInvalidBearerTokenException(InvalidBearerTokenException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message("Access token provided is expired, revoked, malformed, or invalid for other reasons")
                        .build()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Result> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message("No permission")
                        .build()
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    ResponseEntity<Result> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message("API endpoint not found")
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Result> handleOthersExceptions(Exception ex, HttpServletRequest request) {
        LOGGER.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Result.builder()
                        .path(request.getRequestURI())
                        .flag(false)
                        .localDateTime(LocalDateTime.now())
                        .message("Internal Server Error")
                        .build()
        );
    }
}
