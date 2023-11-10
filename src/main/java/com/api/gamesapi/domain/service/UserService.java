package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.UserMapper;
import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.exception.DuplicatedEmailException;
import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.domain.repository.UserRepository;
import com.api.gamesapi.infra.security.TokenService;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    
    Logger logger = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private  boolean loginxists(String login){
        logger.info("Verifying if exists user with login {}", login);
        return userRepository.existsUserByLogin(login);
    }

    public UserResponse saveUser(UserRequest userRequest) {
        logger.info("Creating new user");
        if(loginxists(userRequest.getLogin()))
            throw new DuplicatedEmailException(String.format("There is already user with email %s", userRequest.getLogin()));
        String password = new BCryptPasswordEncoder().encode(userRequest.getPassword());
        User userToBeSaved = userMapper.toEntity(userRequest);
        userToBeSaved.setDataCriacao(OffsetDateTime.now());
        userToBeSaved.setPassword(password);
        return userMapper.toModel(userRepository.save(userToBeSaved));
    }

    public String login(LoginRequest loginRequest) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(loginRequest.getLogin(),loginRequest.getPassword());
        var auth = authenticationManager.authenticate(userNamePassword);
        return tokenService.getToken((User) auth.getPrincipal());
    }

}
