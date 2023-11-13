package com.api.gamesapi.api.model;

import com.api.gamesapi.domain.model.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameRequestDTO {

    @NotNull
    @NotBlank
    @Schema(description = "This is the Game's name", example = "Super Mario World")
    private String name;

    @NotNull
    @Schema(description = "This is the Game's category", example = "PLATFORM")
    private Category category;

    @NotNull
    @Schema(description ="This is the Company's id", example = "1")
    private Long companyId;
}
