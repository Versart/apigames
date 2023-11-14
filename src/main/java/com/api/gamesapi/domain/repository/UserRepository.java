package com.api.gamesapi.domain.repository;

import com.api.gamesapi.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<UserDetails> findByLogin(String login);

    boolean existsUserByLogin(String login);
}
