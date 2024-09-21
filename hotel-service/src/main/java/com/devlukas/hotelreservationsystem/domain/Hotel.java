package com.devlukas.hotelreservationsystem.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "hotelAdmin_id")
    private HotelAdmin hotelAdmin;

    private String name;

    private String email;

    private String phone;

    private String cnpj;

    private boolean isActive;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "hotel", orphanRemoval = true)
    private final List<Amenity> amenities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "hotel", orphanRemoval = true)
    private final List<Assessment> assessments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hotel", orphanRemoval = true)
    private final List<Room> rooms = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hotel", orphanRemoval = true)
    private final List<ImageInfo> images= new ArrayList<>();

    public void addAmenity(Amenity a) {
        this.amenities.add(a);
    }

    public void addAssessment(Assessment a) {
        this.assessments.add(a);
    }

    public void addRoom(Room r) {
        this.rooms.add(r);
    }

    public void addImageUrl(ImageInfo i) {
        this.images.add(i);
    }
}
