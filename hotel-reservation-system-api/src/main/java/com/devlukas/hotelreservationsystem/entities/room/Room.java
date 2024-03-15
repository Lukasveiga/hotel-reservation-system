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
}
