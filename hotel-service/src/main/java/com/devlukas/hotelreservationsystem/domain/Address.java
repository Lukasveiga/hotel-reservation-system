package com.devlukas.hotelreservationsystem.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {

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
}
