package com.api.gamesapi.api.model;

import com.api.gamesapi.domain.model.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRequestDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Category category;

    @NotNull
    private Long companyId;
}
