package com.devlukas.hotelreservationsystem.hotel.entities.room;

import lombok.Getter;

@Getter
public enum Situation {

    AVAILABLE("Available"), UNAVAILABLE("Unavailable");

    private final String situation;

    Situation(String situation) {
        this.situation = situation;
    }

}
