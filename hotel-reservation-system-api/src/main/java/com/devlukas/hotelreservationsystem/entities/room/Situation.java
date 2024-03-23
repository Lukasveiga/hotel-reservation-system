package com.devlukas.hotelreservationsystem.entities.room;

public enum Situation {

    AVAILABLE("Available"), UNAVAILABLE("Unavailable");

    private String situation;

    Situation(String situation) {
        this.situation = situation;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}
