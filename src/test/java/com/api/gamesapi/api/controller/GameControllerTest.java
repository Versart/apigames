package com.api.gamesapi.api.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.GameService;
import com.api.gamesapi.util.GameDTOCreator;

@ExtendWith(SpringExtension.class)
public class GameControllerTest {

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    @BeforeEach
    void setup() {
        PagedModel<EntityModel<GameResponseDTO>> page = GameDTOCreator.createPagedModelGameResponse();
        BDDMockito.when(gameService.getAllGames(ArgumentMatchers.any())).thenReturn(page);
    }

    @Test
    @DisplayName("getAllGames returns pagedModel of GameResponse when successful")
    void getAllGames_ReturnsPagedModelofGameResponse_WhenSuccessful() {
        String nameExpected = GameDTOCreator.createGameResponse().getCompanyName();
        
        PagedModel<EntityModel<GameResponseDTO>> page =  gameController.getAllGames(PageRequest.of(1, 1)).getBody();
        
        Assertions.assertThat(page).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(page.getContent().stream().toList()
            .get(0).getContent().getCompanyName()).isEqualTo(nameExpected);
    }
}
