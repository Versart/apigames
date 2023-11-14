package com.api.gamesapi.domain.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.api.gamesapi.api.mapper.UserMapper;
import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
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
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        String token = "sahsgahsg";

        BDDMockito.when(userMapper.toEntity(ArgumentMatchers.any(UserRequest.class)))
            .thenReturn(UserCreator.createUserAdmin());
        BDDMockito.when(userMapper.toModel(ArgumentMatchers.any(User.class)))
            .thenReturn(UserDTOCreator.createUserResponse());
        BDDMockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
            .thenReturn(UserCreator.createUserAdmin());
        BDDMockito.when(authenticationManager.authenticate(ArgumentMatchers.any(Authentication.class)))
            .thenReturn(authentication);
        BDDMockito.when(authentication.getPrincipal()).thenReturn(UserCreator.createUserAdmin());
        BDDMockito.when(tokenService.getToken(ArgumentMatchers.any(User.class)))
            .thenReturn(token);
    }

    @Test
    void saveUser_ReturnsUserResponse_WhenSuccessful() {
        UserRequest userToBeSaved = UserDTOCreator.createUserRequest();

        UserResponse userSaved = userService.saveUser(userToBeSaved);

        Assertions.assertThat(userSaved).isNotNull();

        Assertions.assertThat(userSaved.getLogin()).isEqualTo(userToBeSaved.getLogin());
    }
    
    @Test
    void login_ReturnsToken_WhenSuccessful() {
        LoginRequest loginRequest = LoginCreator.createLoginRequest();

        String token = userService.login(loginRequest);

        Assertions.assertThat(token).isNotNull().isNotEmpty();
    }

}
