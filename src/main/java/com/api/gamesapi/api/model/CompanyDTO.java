package com.api.gamesapi.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private LocalDate dateOfFoundation;


}
