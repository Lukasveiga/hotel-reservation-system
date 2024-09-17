package com.devlukas.hotelreservationsystem.usecases.exceptions;

public class UniqueIdentifierAlreadyExists extends RuntimeException {
    public UniqueIdentifierAlreadyExists(String message) {
        super("The %s provided has already been registered".formatted(message));
    }
}
