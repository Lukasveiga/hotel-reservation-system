package com.devlukas.hotelreservationsystem.entities.room;

import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private Double size;

    private Integer bedsNumber;

    private BigDecimal price;

    private Integer maxGuestsNumber;

    @Enumerated(EnumType.STRING)
    private Situation situation;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Hotel hotel;

    public Room() {
    }

    public Room(String name, double size, int bedsNumber, int i, int maxGuestsNumber, Situation available) {
    }

    public Room(String name, Double size, Integer bedsNumber, BigDecimal price, Integer maxGuestsNumber, Situation situation) {
        this.name = name;
        this.size = size;
        this.bedsNumber = bedsNumber;
        this.price = price;
        this.maxGuestsNumber = maxGuestsNumber;
        this.situation = situation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Integer getBedsNumber() {
        return bedsNumber;
    }

    public void setBedsNumber(Integer bedsNumber) {
        this.bedsNumber = bedsNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMaxGuestsNumber() {
        return maxGuestsNumber;
    }

    public void setMaxGuestsNumber(Integer maxGuestsNumber) {
        this.maxGuestsNumber = maxGuestsNumber;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
