package com.devlukas.hotelreservationsystem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room", orphanRemoval = true)
    private final List<ImageInfo> images= new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Hotel hotel;
}
