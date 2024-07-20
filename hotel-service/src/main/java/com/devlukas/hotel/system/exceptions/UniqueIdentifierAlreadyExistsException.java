package com.devlukas.hotel.system.exceptions;

public class UniqueIdentifierAlreadyExistsException extends RuntimeException {

    public UniqueIdentifierAlreadyExistsException(String uniqueIdentifier) {
        super("The %s provided has already been registered".formatted(uniqueIdentifier));
    }
}
