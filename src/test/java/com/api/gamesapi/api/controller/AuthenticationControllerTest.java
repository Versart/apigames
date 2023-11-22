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
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.exception.DuplicatedEmailException;
import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.domain.service.UserService;
import com.api.gamesapi.util.LoginCreator;
import com.api.gamesapi.util.UserCreator;
import com.api.gamesapi.util.UserDTOCreator;

@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {
    
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private  UserService userServiceMock;

    @BeforeEach
    void setup() {
        String token = "shagshg1212hgjsgd";
        User user = UserCreator.createUserAdmin();
        
        UserResponse userResponse = UserDTOCreator.createUserResponse();
        
        BDDMockito.when(userServiceMock.saveUser(ArgumentMatchers.any(UserRequest.class)))
            .thenReturn(userResponse);
        
            BDDMockito.when(userServiceMock.login(ArgumentMatchers.any(LoginRequest.class)))
            .thenReturn(token);
    }

    @Test
    @DisplayName("login returns token when successful")
    void login_ReturnsToken_WhenSuccessful() {
        LoginRequest loginRequest = LoginCreator.createLoginRequest();

        String token = authenticationController.login(loginRequest).getBody();

        Assertions.assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("login throws AuthenticationException when authentication fails")
    void login_ThrowsAuthenticationException_WhenAuthenticationFails() {
        BDDMockito.when(userServiceMock.login(ArgumentMatchers.any(LoginRequest.class)))
            .thenThrow(AuthenticationFailed.class);
            
        LoginRequest loginRequest = LoginCreator.createLoginRequest();
        
        Assertions.assertThatExceptionOfType(AuthenticationException.class)
            .isThrownBy(() -> authenticationController.login(loginRequest));
        
    }
    
    @Test
    @DisplayName("register returns UserResponse when successful")
    void register_ReturnsUserResponse_WhenSuccessful() {
        UserRequest userToBeSaved = UserDTOCreator.createUserRequest();
        
        UserResponse userResponse = authenticationController.register(userToBeSaved).getBody();

        Assertions.assertThat(userResponse).isNotNull();

        Assertions.assertThat(userResponse.getLogin()).isEqualTo(userToBeSaved.getLogin());
    }

    @Test
    @DisplayName("register throws DuplicatedEmailException when there is already user with email")
    void register_ThrowsDuplicatedEmailException_WhenThereIsAlreadyUserWithEmail() {
        BDDMockito.when(userServiceMock.saveUser(ArgumentMatchers.any(UserRequest.class)))
            .thenThrow(DuplicatedEmailException.class);

        UserRequest userRequest = UserDTOCreator.createUserRequest();

        Assertions.assertThatExceptionOfType(DuplicatedEmailException.class)
            .isThrownBy(() -> authenticationController.register(userRequest));
    }

    class AuthenticationFailed extends AuthenticationException{
        public AuthenticationFailed(String msg){
            super(msg);
        }
    }

}
