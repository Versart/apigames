package com.api.gamesapi.integration;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.DefaultResponseErrorHandler;
import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.model.User;
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
    @Qualifier(value = "testRestTemplate")
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;    

    @TestConfiguration()
    @Lazy
    static class RestTemplateBuilderConfiguration {
        @Bean(name = "testRestTemplate")
        TestRestTemplate restTemplateBuilder(@Value("${local.server.port}") int port) {
            return new TestRestTemplate(new RestTemplateBuilder().errorHandler(new DefaultResponseErrorHandler()
            )
            .requestFactory(HttpComponentsClientHttpRequestFactory.class)
            .rootUri("http://localhost:" + port));
        }
    }

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
    @DisplayName("login throws unauthorized when login fails")
    void login_ThrowsUnauthorized_WhenLoginFails() {
        LoginRequest loginRequest = LoginCreator.createLoginRequest();
        
        String url = "/auth/login";
        
        ResponseEntity<String> exchange = testRestTemplate.exchange(url, HttpMethod.POST, 
        new HttpEntity<>(loginRequest), new ParameterizedTypeReference<>() {   
        });
      
        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

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

    @Test
    @DisplayName("register throws DuplicatedEmailException when there is already user with email")
    void register_ThrowsDuplicatedEmailException_WhenThereIsAlreadyUserWithEmail() {
        String url = "/auth/register";
        
        UserRequest userToBeSaved = UserDTOCreator.createUserRequest();

        User user = UserCreator.createUserAdmin();
        
        userRepository.save(user);

        ResponseEntity<UserResponse> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
             new HttpEntity<>(userToBeSaved), new ParameterizedTypeReference<>() {
                
             });
        
        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

    }


    
    @AfterEach
    void end() {
        userRepository.deleteAll();
    }
}
