package com.api.gamesapi.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotNull
    @NotBlank
    @Schema(description = "This is the User's login", example = "mario123")
    private String login;
    @NotNull
    @NotBlank
    @Schema(description = "This is the User's password", example = "peach123")
    private String password;
}
