package com.api.gamesapi.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class CompanyDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private LocalDate dateOfFoundation;
}
