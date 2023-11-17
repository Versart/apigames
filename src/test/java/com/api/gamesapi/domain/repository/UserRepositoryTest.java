package com.api.gamesapi.domain.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.util.UserCreator;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("existsUserByLogin returns true when successful")
    void existsUserByLogin_ReturnsTrue_WhenSuccessful() {
        User userSaved = userRepository.save(UserCreator.createUserAdmin());
        
        String loginExpected = userSaved.getLogin();

        boolean existsUser  = userRepository.existsUserByLogin(loginExpected);

        Assertions.assertThat(existsUser).isTrue();

    }

    @Test
    @DisplayName("existsUserByLogin returns false when user doen not exists")
    void existsUserByLogin_ReturnsFalse_WhenUserDoesNotExists() {
        String loginExpected = "maria";

        boolean existsUser  = userRepository.existsUserByLogin(loginExpected);

        Assertions.assertThat(existsUser).isFalse();

    }

    @Test
    @DisplayName("findByLogin returns Optional with User when successful")
    void findByLogin_ReturnsOptionalWithUser_WhenSuccessful() {
        User userSaved = userRepository.save(UserCreator.createUserAdmin());
        String loginExpected = userSaved.getLogin();

        Optional<UserDetails> userExpected = userRepository.findByLogin(loginExpected);

        Assertions.assertThat(userExpected).isNotEmpty();

        Assertions.assertThat(userExpected.get().getUsername()).isEqualTo(loginExpected);
    }
    

    @Test
    @DisplayName("findByLogin returns empty Optional when User does not exists")
    void findByLogin_ReturnsEmptyOptional_WhenUserDoesNotExists() {
        String loginExpected = "maria";

        Optional<UserDetails> userExpected = userRepository.findByLogin(loginExpected);

        Assertions.assertThat(userExpected).isEmpty();
    }
    
}
