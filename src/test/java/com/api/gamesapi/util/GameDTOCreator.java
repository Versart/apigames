package com.api.gamesapi.util;

import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;

import java.util.List;

public class GameDTOCreator {

    public static PagedModel<EntityModel<GameResponseDTO>> createPagedModelGameResponse() {
        List<EntityModel<GameResponseDTO>> listCompanyDTO = List.of(createEntityModelGameResponse());

        return PagedModel.of(listCompanyDTO, new PagedModel.PageMetadata(10, 0, 0)
            ,Link.of("/games/"));
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
