package com.devlukas.hotel.hotel.entities.room;

import lombok.Getter;

@Getter
public enum Situation {

    AVAILABLE("Available"), UNAVAILABLE("Unavailable");

    private final String situation;

    Situation(String situation) {
        this.situation = situation;
    }

    public String getValue() {
        return situation;
    }
}
