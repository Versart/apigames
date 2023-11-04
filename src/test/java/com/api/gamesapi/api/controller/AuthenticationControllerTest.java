package com.api.gamesapi.api.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.domain.service.UserService;
import com.api.gamesapi.infra.security.TokenService;
import com.api.gamesapi.util.LoginCreator;
import com.api.gamesapi.util.UserCreator;

@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {
    
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private  UserService userService;

    @Mock
    private  AuthenticationManager authenticationManager;

    @Mock
    private  TokenService tokenService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        String token = "shagshg1212hgjsgd";
        User user = UserCreator.createUserAdmin();
        BDDMockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        BDDMockito.when(authentication.getPrincipal()).thenReturn(user);
        BDDMockito.when(tokenService.getToken(ArgumentMatchers.any(User.class)))
            .thenReturn(token);
    }

    @Test
    @DisplayName("login returns token when successful")
    void login_ReturnsToken_WhenSuccessful() {
        LoginRequest loginRequest = LoginCreator.createLoginRequest();

        String token = authenticationController.login(loginRequest).getBody();

        Assertions.assertThat(token).isNotNull().isNotEmpty();
    }

}
