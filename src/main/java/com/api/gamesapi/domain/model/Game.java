package com.api.gamesapi.domain.model;

import com.api.gamesapi.domain.validationgroup.ValidationGroup;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "company_id")
    @Valid
    @ConvertGroup(from = Default.class, to = ValidationGroup.CompanyId.class)
    private Company company;

}
