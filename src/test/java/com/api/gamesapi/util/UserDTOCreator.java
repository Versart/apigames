package com.api.gamesapi.util;

import java.time.OffsetDateTime;

import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.model.UserRole;

public class UserDTOCreator {
    
    public static UserRequest createUserRequest() {
        return UserRequest
            .builder()
            .login("maria")
            .password("12345678")
            .role(UserRole.ADMIN)
            .build();
    }

    public static UserResponse createUserResponse() {
        return UserResponse
            .builder()
            .login("maria")
            .dataCriacao(OffsetDateTime.now())
            .build();
    }
}
