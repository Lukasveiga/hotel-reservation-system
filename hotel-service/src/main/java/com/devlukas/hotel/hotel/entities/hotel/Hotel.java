package com.devlukas.hotel.hotel.entities.hotel;

import com.devlukas.hotel.hotel.entities.room.Room;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String CNPJ;

    private String phone;

    private String email;

    private String description;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @JsonManagedReference
    private HotelAddress address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "hotel", orphanRemoval = true)
    private final Set<Assessment> assessments = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "hotel", orphanRemoval = true)
    private final Set<Convenience> conveniences = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Room> rooms = new ArrayList<>();

    public Hotel() {
    }

    public Hotel(String name, String CNPJ, String phone, String email, String description, HotelAddress address) {
        this.name = name;
        this.CNPJ = CNPJ;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.address = address;
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public void addConveniences(Convenience convenience) {
        this.conveniences.add(convenience);
    }
    public void addAssessment(Assessment assessment) {
        this.assessments.add(assessment);
    }
}
