package com.api.gamesapi.api.model;

import com.api.gamesapi.domain.model.UserRole;
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
        private String login;
        @NotNull
        @NotBlank
        @Size(min = 8)
        private String password;
        @NotNull
        private UserRole role;
}
