package com.api.gamesapi.api.mapper;

import com.api.gamesapi.api.controller.GameController;
import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.model.Game;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
@RequiredArgsConstructor
public class GameMapper {


    private final ModelMapper modelMapper;


    public EntityModel<GameResponseDTO> toModelResponse(Game game) {

        return EntityModel.of(modelMapper.map(game, GameResponseDTO.class),
                linkTo(methodOn(GameController.class).getGameById(game.getId())).withSelfRel(),
                linkTo(methodOn(GameController.class).listGames()).withRel("All Games")
               );
    }

    public CollectionModel<EntityModel<GameResponseDTO>> toModelResponseList(List<Game> games){
        return CollectionModel.of(games.stream().map(
                this::toModelResponse
        ).collect(Collectors.toList()),linkTo(methodOn(GameController.class).listGames()).withSelfRel());
    }

    public Game toEntity(GameRequestDTO gameRequesteDTO){
        return modelMapper.map(gameRequesteDTO,Game.class);
    }
}
