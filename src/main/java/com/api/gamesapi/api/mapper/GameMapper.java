package com.api.gamesapi.api.mapper;

import com.api.gamesapi.api.controller.GameController;
import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.model.Game;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
@RequiredArgsConstructor
public class GameMapper {


    private final ModelMapper modelMapper;

    private final PagedResourcesAssembler<Game> pagedResourcesAssembler;

    public EntityModel<GameResponseDTO> toModelResponse(Game game) {

        return EntityModel.of(modelMapper.map(game, GameResponseDTO.class),
                linkTo(methodOn(GameController.class).getGameById(game.getId())).withSelfRel(),
                linkTo(methodOn(GameController.class).getAllGames(PageRequest.of(0, 10))).withRel("All Games")
               );
    }

    public PagedModel<EntityModel<GameResponseDTO>> toPagedModel(Page<Game> games){
        return pagedResourcesAssembler.toModel(games, this::toModelResponse);
    }


    public Game toEntity(GameRequestDTO gameRequesteDTO){
        return modelMapper.map(gameRequesteDTO,Game.class);
    }
}
