package com.devlukas.hotelreservationsystem.services.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, Long id) {
        super("Could not found %s with id %d".formatted(objectName, id));
    }
}
