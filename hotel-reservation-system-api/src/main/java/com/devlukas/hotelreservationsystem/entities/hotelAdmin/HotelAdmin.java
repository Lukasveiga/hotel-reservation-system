package com.devlukas.hotelreservationsystem.entities.hotelAdmin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class HotelAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String CNPJ;

    private String password;

    private String roles;

    public HotelAdmin() {
    }

}
