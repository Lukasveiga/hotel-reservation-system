package com.devlukas.hotelreservationsystem.services.hotel.exceptions;

public class UniqueIdentifierAlreadyExistsException extends RuntimeException {

    public UniqueIdentifierAlreadyExistsException() {
        super("The CNPJ provided has already been registered in the database");
    }
}
