package com.api.gamesapi.api.model;

import com.api.gamesapi.domain.model.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest{
        @NotNull
        @NotBlank
        @Schema(description = "This is the User's login", example = "mario123")
        private String login;
        @NotNull
        @NotBlank
        @Size(min = 8)
        @Schema(description = "This is the User's password", example = "peach123")
        private String password;
        @NotNull
        @Schema(description = "This is the User's role", example = "USER")
        private UserRole role;
}
