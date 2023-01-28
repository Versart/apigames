package com.api.gamesapi.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private LocalDate DateOfFoundation;

    @OneToMany(mappedBy = "company", cascade= CascadeType.ALL)
    private List<Game> games;
}
