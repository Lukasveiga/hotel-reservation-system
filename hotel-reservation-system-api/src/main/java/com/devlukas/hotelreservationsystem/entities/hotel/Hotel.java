package com.devlukas.hotelreservationsystem.entities.hotel;

import com.devlukas.hotelreservationsystem.entities.room.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
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


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private HotelAddress address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hotel")
    private final Set<Assessments> assessments = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_conveniences",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "convenience_id")
    )
    private final Set<Conveniences> conveniences = new HashSet<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private final List<Room> rooms = new ArrayList<>();

    @Version
    protected long version;

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
}
