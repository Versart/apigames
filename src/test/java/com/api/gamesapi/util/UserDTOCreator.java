package com.api.gamesapi.util;

import com.api.gamesapi.api.model.UserRequest;
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
}
