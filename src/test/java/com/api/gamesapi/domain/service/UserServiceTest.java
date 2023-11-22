package com.api.gamesapi.domain.service;


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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.api.gamesapi.api.mapper.UserMapper;
import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.exception.DuplicatedEmailException;
import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.domain.repository.UserRepository;
import com.api.gamesapi.infra.security.TokenService;
import com.api.gamesapi.util.LoginCreator;
import com.api.gamesapi.util.UserCreator;
import com.api.gamesapi.util.UserDTOCreator;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserMapper userMapperMock;

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private TokenService tokenServiceMock;

    @BeforeEach
    void setup() {
        String token = "sahsgahsg";

        BDDMockito.when(userMapperMock.toEntity(ArgumentMatchers.any(UserRequest.class)))
            .thenReturn(UserCreator.createUserAdmin());
        
        BDDMockito.when(userMapperMock.toModel(ArgumentMatchers.any(User.class)))
        .thenReturn(UserDTOCreator.createUserResponse());
    
        BDDMockito.when(userRepositoryMock.save(ArgumentMatchers.any(User.class)))
        .thenReturn(UserCreator.createUserAdmin());
    
        BDDMockito.when(authenticationManagerMock.authenticate(ArgumentMatchers.any(Authentication.class)))
        .thenReturn(authenticationMock);
    
        BDDMockito.when(authenticationMock.getPrincipal()).thenReturn(UserCreator.createUserAdmin());
    
        BDDMockito.when(tokenServiceMock.getToken(ArgumentMatchers.any(User.class)))
        .thenReturn(token);
    
        BDDMockito.when(userRepositoryMock.existsUserByLogin(ArgumentMatchers.anyString()))
        .thenReturn(false);
    }

    @Test
    @DisplayName("loginExists returns true when User exists")
    void loginExists_ReturnsTrue_WhenUserExists() {
        BDDMockito.when(userRepositoryMock.existsUserByLogin(ArgumentMatchers.anyString()))
            .thenReturn(true);
        
        String login = LoginCreator.createLoginRequest().getLogin();

        boolean exists = userService.loginxists(login);

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("loginExists returns false when User not exists")
    void loginExists_ReturnsFalse_WhenUserExists() {
        String login = "";

        boolean exists = userService.loginxists(login);

        Assertions.assertThat(exists).isFalse();

    }


    @Test
    @DisplayName("saveUser returns UserResponse when successful")
    void saveUser_ReturnsUserResponse_WhenSuccessful() {
        UserRequest userToBeSaved = UserDTOCreator.createUserRequest();

        UserResponse userSaved = userService.saveUser(userToBeSaved);

        Assertions.assertThat(userSaved).isNotNull();

        Assertions.assertThat(userSaved.getLogin()).isEqualTo(userToBeSaved.getLogin());
    }

    @Test
    @DisplayName("saveUser throws DuplicatedEmailException when where there is already user with email")
    void saveUser_ThrowsDuplicatedEmailException_WhenThereIsAlreadyUserWithEmail() {
        BDDMockito.when(userRepositoryMock.existsUserByLogin(ArgumentMatchers.anyString()))
            .thenReturn(true);
        
        UserRequest userRequest = UserDTOCreator.createUserRequest();

        Assertions.assertThatExceptionOfType(DuplicatedEmailException.class)
            .isThrownBy(() -> userService.saveUser(userRequest));
    }

    
    @Test
    @DisplayName("login returns Token when successful")
    void login_ReturnsToken_WhenSuccessful() {
        LoginRequest loginRequest = LoginCreator.createLoginRequest();

        String token = userService.login(loginRequest);

        Assertions.assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("login throws AuthenticationException when user is not found")
    void login_ThrowsAuthenticationException_WhenUserIsNotFound() {
        BDDMockito.when(authenticationManagerMock.authenticate(ArgumentMatchers.any(Authentication.class)))
            .thenThrow(AuthenticationFailed.class);

        LoginRequest loginRequest = LoginCreator.createLoginRequest();

        Assertions.assertThatExceptionOfType(AuthenticationFailed.class)
            .isThrownBy(() -> userService.login(loginRequest));
    }

    class AuthenticationFailed extends AuthenticationException{
        public AuthenticationFailed(String msg){
            super(msg);
        }
    }


}
