package com.devlukas.system.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, Long id) {
        super("Could not found %s with id %d".formatted(objectName, id));
    }

    public ObjectNotFoundException(String objectName, String id) {
        super("Could not found %s with id %s".formatted(objectName, id));
    }
}
