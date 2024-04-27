package com.devlukas.hotelreservationsystem.system.exceptions;

public class UniqueIdentifierAlreadyExistsException extends RuntimeException {

    public UniqueIdentifierAlreadyExistsException(String uniqueIdentifier) {
        super("The %s provided has already been registered in the database".formatted(uniqueIdentifier));
    }
}
