package com.devlukas.hotel.hotel.entities.room;

import com.devlukas.hotel.hotel.entities.hotel.Hotel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    private Integer price;

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

    public Room(String name, Double size, Integer bedsNumber, Integer price, Integer maxGuestsNumber, Situation situation) {
        this.name = name;
        this.size = size;
        this.bedsNumber = bedsNumber;
        this.price = price;
        this.maxGuestsNumber = maxGuestsNumber;
        this.situation = situation;
    }
}
