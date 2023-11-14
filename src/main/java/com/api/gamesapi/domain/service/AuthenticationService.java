package com.api.gamesapi.domain.service;

import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    Logger logger = LogManager.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Fetching user with username {}", username);
        return userRepository.findByLogin(username).orElseThrow(() -> new NotFoundException(String
            .format("User not found with login %s", username)));
    }


}
