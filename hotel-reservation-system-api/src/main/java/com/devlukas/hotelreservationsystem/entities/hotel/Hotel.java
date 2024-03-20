package com.devlukas.hotelreservationsystem.entities.hotel;

import com.devlukas.hotelreservationsystem.entities.room.Room;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
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


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private HotelAddress address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hotel")
    private final Set<Assessments> assessments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "hotel_conveniences",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "convenience_id")
    )
    private final Set<Conveniences> conveniences = new HashSet<>();

    @OneToMany(mappedBy = "hotel")
    private final Set<Room> rooms = new HashSet<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HotelAddress getAddress() {
        return address;
    }

    public void setAddress(HotelAddress address) {
        this.address = address;
    }

    public String getCNPJ() {
        return CNPJ;
    }
}
