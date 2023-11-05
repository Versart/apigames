package com.api.gamesapi.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;

import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.repository.UserRepository;
import com.api.gamesapi.domain.service.UserService;
import com.api.gamesapi.infra.security.TokenService;
import com.api.gamesapi.util.UserCreator;
import com.api.gamesapi.util.UserDTOCreator;
import com.api.gamesapi.util.LoginCreator;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AuthenticationControllerIT {
    
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("login returns token when successful")
    void login_ReturnsToken_WhenSuccessful() {
        LoginRequest loginRequest = LoginCreator.createLoginRequest();
        String url = "/auth/login";
        userRepository.save(UserCreator.createUserAdmin());

        ResponseEntity<String> exchange = testRestTemplate.exchange(url, HttpMethod.POST, 
            new HttpEntity<>(loginRequest), new ParameterizedTypeReference<>() {   
        });

        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(exchange.getBody()).isNotNull().isNotBlank();
    }

    @Test
    @DisplayName("register returns UserResponse when successful")
    void register_ReturnsUserResponse_WhenSuccessful() {
        String url = "/auth/register";
        UserRequest userToBeSaved = UserDTOCreator.createUserRequest();

        ResponseEntity<UserResponse> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
             new HttpEntity<>(userToBeSaved), new ParameterizedTypeReference<>() {
                
             });
        
        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(exchange.getBody()).isNotNull();

        Assertions.assertThat(exchange.getBody().getLogin()).isEqualTo(userToBeSaved.getLogin());
    }

    
    @AfterEach
    void end() {
        userRepository.deleteAll();
    }
}
