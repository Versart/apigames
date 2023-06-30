package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.UserMapper;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.exception.DuplicatedEmailException;
import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private  boolean loginxists(String login){
        return userRepository.existsUserByLogin(login);
    }

    public UserResponse saveUser(UserRequest userRequest) {
        if(loginxists(userRequest.getLogin()))
            throw new DuplicatedEmailException("Email já utilizado!");
        String password = new BCryptPasswordEncoder().encode(userRequest.getPassword());
        User userToBeSaved = userMapper.toEntity(userRequest);
        userToBeSaved.setDataCriacao(OffsetDateTime.now());
        userToBeSaved.setPassword(password);
        return userMapper.toModel(userRepository.save(userToBeSaved));
    }

}
