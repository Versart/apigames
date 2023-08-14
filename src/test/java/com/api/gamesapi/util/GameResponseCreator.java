package com.api.gamesapi.util;

import com.api.gamesapi.api.model.GameResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public class GameResponseCreator {

    public static CollectionModel<EntityModel<GameResponseDTO>> createCollectionModelGameResponse() {
        return CollectionModel.of(List.of(createEntityModelGameResponse()));
    }

    public static EntityModel<GameResponseDTO> createEntityModelGameResponse() {
        return EntityModel.of(createGameResponse());
    }

    public static GameResponseDTO createGameResponse() {
        return GameResponseDTO.builder()
                .companyName("Companhia Teste Z")
                .name("Game Z").build();
    }
}
