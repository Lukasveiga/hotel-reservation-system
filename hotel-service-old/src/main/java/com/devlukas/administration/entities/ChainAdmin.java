package com.devlukas.administration.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"CNPJ"}))
public class ChainAdmin extends Admin {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chainAdmin", orphanRemoval = true)
    private final List<LocalAdmin> localAdmins = new ArrayList<>();

    public void addLocalAdmin(LocalAdmin la) {
        this.localAdmins.add(la);
    }
}
