package com.devlukas.hotelreservationsystem.entities.room;

public enum Situation {

    AVAILABLE("Available"), UNAVAILABLE("Unavailable"), PENDING("Pending");

    private final String situation;

    Situation(String situation) {
        this.situation = situation;
    }

    public String getSituation() {
        return situation;
    }
}
