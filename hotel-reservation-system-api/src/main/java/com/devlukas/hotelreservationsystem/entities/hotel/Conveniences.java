package com.devlukas.hotelreservationsystem.entities.hotel;


import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Conveniences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String description;

    @ManyToMany(mappedBy = "conveniences")
    private Set<Hotel> hotel;
}
