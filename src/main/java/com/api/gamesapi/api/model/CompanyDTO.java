package com.api.gamesapi.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {

    private Long id;

    @NotNull
    @NotBlank
    @Schema(description = "This is company's name", example = "Nintendo")
    private String name;

    @NotNull
    @Schema(description = "This is the founding date of the company", example = "1889-09-23")
    private LocalDate dateOfFoundation;


}
