package com.devlukas.hotelreservationsystem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HotelAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String phone;

    private String cnpj;

    private String roles;

    private boolean isActive;

    @OneToMany(mappedBy = "hotelAdmin", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private final List<Hotel> hotels = new ArrayList<>();

    public void addHotel(Hotel hotel) {
        this.hotels.add(hotel);
    }
}
