package com.api.gamesapi.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @Schema(description = "This is the User's login", example = "mario123")
    private String login;
    @Schema(description = "This is the creation date of the User", example = "2023-11-13T14:16:51.055Z")
    private OffsetDateTime dataCriacao;
}
