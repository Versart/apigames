package com.api.gamesapi.util;

import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.model.Category;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import java.util.List;

public class GameDTOCreator {

    private static String name = "Super Mario World";
    private static String companyName = "Nintendo";

    public static PagedModel<EntityModel<GameResponseDTO>> createPagedModelGameResponse() {
        List<EntityModel<GameResponseDTO>> listCompanyDTO = List.of(createEntityModelGameResponse());

        return PagedModel.of(listCompanyDTO, new PagedModel.PageMetadata(10, 0, 0)
            ,Link.of("/games/"));
    }

    public static EntityModel<GameResponseDTO> createEntityModelGameResponse() {
        return EntityModel.of(createGameResponse(),Link.of("/games/"));
    }

    public static GameResponseDTO createGameResponse() {
        return GameResponseDTO.builder()
                .id(1l)
                .companyName(companyName)
                .name(name)
                .build();
    }

    public static GameRequestDTO createGameRequest() {
        return GameRequestDTO.builder()
                    .category(Category.PLATFORM)
                    .name(name)
                    .companyId(1l)
                    .build();
    }

    public static GameRequestDTO createAlteredGameRequest() {
        return GameRequestDTO.builder()
                    .category(Category.PLATFORM)
                    .companyId(1l)
                    .name("Super Mario World 2")
                    .build();
    }

    public static GameResponseDTO createAlteredGameResponse() {
        return GameResponseDTO.builder()
                    .companyName(companyName)
                    .name("Super Mario World 2")
                    .id(1l)
                    .build();
    }

    public static EntityModel<GameResponseDTO> createEntityModelAlteredGameResponse() {
        return EntityModel.of(createAlteredGameResponse(),Link.of("/games/") );
    }
}
