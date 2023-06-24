package com.api.gamesapi.domain.model;

import com.api.gamesapi.domain.validationgroup.ValidationGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = ValidationGroup.CompanyId.class)
    private Long id;
    @NotBlank
    @NotNull(message = "must not be null")
    private String name;

    @NotNull
    private LocalDate dateOfFoundation;

    @OneToMany(mappedBy = "company", cascade= CascadeType.ALL)
    private List<Game> games;
}
