package com.api.gamesapi.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;

}
