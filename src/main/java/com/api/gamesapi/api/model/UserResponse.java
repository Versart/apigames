package com.api.gamesapi.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class UserResponse {
    private String login;
    private OffsetDateTime dataCriacao;


}
