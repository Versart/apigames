package com.api.gamesapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.domain.model.UserRole;

public class UserCreator {
    
    public static User createUserAdmin() {
        String senha = new BCryptPasswordEncoder().encode("12345678");
        return User.builder().login("maria").password(senha).role(UserRole.ADMIN).build();
        
    }
}
