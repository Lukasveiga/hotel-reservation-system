package com.devlukas.hotelreservationsystem.entities.room;

import com.devlukas.hotelreservationsystem.entities.hotel.Hotel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
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

    @Version
    protected long version;

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
}
