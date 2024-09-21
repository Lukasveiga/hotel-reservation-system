package com.devlukas.hotelreservationsystem.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer rating;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Hotel hotel;
}
