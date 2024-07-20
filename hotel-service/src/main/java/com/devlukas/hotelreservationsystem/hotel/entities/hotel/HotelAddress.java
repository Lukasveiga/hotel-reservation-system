package com.devlukas.hotelreservationsystem.hotel.entities.hotel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
    @JsonBackReference
    private Hotel hotel;

    public HotelAddress() {
    }

    public HotelAddress(String country, String state, String city, String district, String street, String number, String zipCode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.district = district;
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
    }
}
