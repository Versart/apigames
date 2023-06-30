package com.api.gamesapi.api.mapper;

import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;


    public User toEntity(UserRequest userRequest){
        return modelMapper.map(userRequest, User.class);
    }

    public UserResponse toModel(User user) {
        return modelMapper.map(user,UserResponse.class);
    }
}
