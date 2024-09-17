package com.devlukas.hotelreservationsystem.usecases.exceptions;

public class UniqueIdentifierAlreadyExistsException extends RuntimeException {
    public UniqueIdentifierAlreadyExistsException(String message) {
        super("The %s provided has already been registered".formatted(message));
    }
}
