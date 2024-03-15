package com.devlukas.hotelreservationsystem.entities.hotel;

import jakarta.persistence.*;

@Entity
public class HotelAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String country;

    private String state;

    private String city;

    private String district;

    private String street;

    private String number;

    private String zipCode;

    @OneToOne(mappedBy = "address")
    private Hotel hotel;
}
