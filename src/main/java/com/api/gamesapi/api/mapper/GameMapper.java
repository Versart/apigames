package com.api.gamesapi.api.mapper;

import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.model.Game;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameMapper {

    @Autowired
    private ModelMapper modelMapper;


    public GameResponseDTO toModelResponse(Game game) {
        return modelMapper.map(game, GameResponseDTO.class);
    }

    public List<GameResponseDTO> toModelResponseList(List<Game> games){
        return games.stream().map(
                game -> modelMapper.map(game, GameResponseDTO.class)
        ).collect(Collectors.toList());
    }

    public Game toEntity(GameRequestDTO gameRequesteDTO){
        return modelMapper.map(gameRequesteDTO,Game.class);
    }
}
