package com.api.gamesapi.domain.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.domain.repository.UserRepository;
import com.api.gamesapi.util.UserCreator;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {
    
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        UserDetails userDetails = UserCreator.createUserAdmin();
        BDDMockito.when(userRepository.findByLogin(ArgumentMatchers.anyString()))
            .thenReturn(Optional.of(userDetails));
    }

    @Test
    void loadUserByUsername_ReturnsUserDetails_WhenSuccessful() {
        String nameExpected = UserCreator.createUserAdmin().getLogin();

        UserDetails user = authenticationService.loadUserByUsername(nameExpected);

        Assertions.assertThat(user).isNotNull();

        Assertions.assertThat(user.getUsername()).isEqualTo(nameExpected);
    }

    @Test
    void loadUserByUsername_ThrowsNotFoundException_WhenUserDoesNotExists() {
        BDDMockito.when(userRepository.findByLogin(ArgumentMatchers.anyString()))
            .thenReturn(Optional.empty());
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> authenticationService.loadUserByUsername("maria"));
        
    }
}
