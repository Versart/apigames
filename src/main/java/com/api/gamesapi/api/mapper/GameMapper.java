package com.api.gamesapi.api.mapper;

import com.api.gamesapi.api.model.GameDTO;
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


    public GameDTO toModel(Game game) {
        return modelMapper.map(game, GameDTO.class);
    }

    public List<GameDTO> toModelList(List<Game> games){
        return games.stream().map(
                game -> modelMapper.map(game, GameDTO.class)
        ).collect(Collectors.toList());
    }
}
