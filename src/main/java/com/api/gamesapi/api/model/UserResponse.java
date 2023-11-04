package com.api.gamesapi.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class UserResponse {
    private String login;
    private OffsetDateTime dataCriacao;


}
