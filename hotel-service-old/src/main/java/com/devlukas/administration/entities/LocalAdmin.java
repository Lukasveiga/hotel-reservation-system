package com.devlukas.administration.entities;

import com.devlukas.hotel.entities.hotel.Hotel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LocalAdmin extends Admin {

    @ManyToOne
    @JoinColumn(name = "chain_admin_id")
    private ChainAdmin chainAdmin;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @JsonManagedReference
    private Hotel hotel;
}
