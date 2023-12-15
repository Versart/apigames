package com.api.gamesapi.util;

import com.api.gamesapi.api.model.LoginRequest;

public class LoginCreator {
    

    public static LoginRequest createLoginRequest() {
        return LoginRequest
            .builder()
            .login("maria")
            .password("12345678")
            .build();
    }
}
