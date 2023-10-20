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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.service.GameService;
import com.api.gamesapi.util.GameDTOCreator;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class GameControllerTest {

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    @BeforeEach
    void setup() {
        PagedModel<EntityModel<GameResponseDTO>> page = GameDTOCreator.createPagedModelGameResponse();
        EntityModel<GameResponseDTO> gameResponse = GameDTOCreator.createEntityModelGameResponse();
        BDDMockito.when(gameService.getAllGames(ArgumentMatchers.any())).thenReturn(page);
        BDDMockito.when(gameService.searchGameById(ArgumentMatchers.anyLong())).thenReturn(gameResponse);
        BDDMockito.when(gameService.saveGame(ArgumentMatchers.any(GameRequestDTO.class))).thenReturn(gameResponse);
        BDDMockito.when(gameService.updateGameById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(
            GameRequestDTO.class
        ))).thenReturn(GameDTOCreator.createEntityModelAlteredGameResponse());
        BDDMockito.doNothing().when(gameService).deleteGameById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("getAllGames returns PagedModel of GameResponse when successful")
    void getAllGames_ReturnsPagedModelofGameResponse_WhenSuccessful() {
        String expectedName = GameDTOCreator.createGameResponse().getCompanyName();
        
        PagedModel<EntityModel<GameResponseDTO>> page =  gameController.getAllGames(PageRequest.of(1, 1)).getBody();
        
        Assertions.assertThat(page).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(page.getContent().stream().toList()
            .get(0).getContent().getCompanyName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("getAllGames returns empty PagedModel of GameResponse when no game is found")
    void getAllGames_ReturnsEmptyPagedModelOfResponse_WhenNoGameIsFound() {
        BDDMockito.when(gameService.getAllGames(ArgumentMatchers.any())).thenReturn(PagedModel.empty());

        PagedModel<EntityModel<GameResponseDTO>> page = gameController.getAllGames(PageRequest.of(1, 1)).getBody();

        Assertions.assertThat(page).isNotNull().isEmpty();;

    }


    @Test
    @DisplayName("getGameById returns EntityModel of GameResponse when successful")
    void getGameById_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        Long expectedId = GameDTOCreator.createGameResponse().getId();

        EntityModel<GameResponseDTO> game = gameController.getGameById(1l).getBody();

        Assertions.assertThat(game).isNotNull();

        Assertions.assertThat(game.getContent().getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("getGameById throws NotFoundException when game is not found")
    void getGameById_ThrowsNotFoundException_WhenGameIsNotFound() {
        BDDMockito.when(gameService.searchGameById(ArgumentMatchers.anyLong())).thenThrow(NotFoundException.class);

        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(
            () -> gameService.searchGameById(1l)
        );
        
    }

    @Test
    @DisplayName("saveGame returns EntityModel of GameResponse when successful")
    void saveGame_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        GameRequestDTO gameToBeSaved = GameDTOCreator.createGameRequest();

        EntityModel<GameResponseDTO> gameSaved = gameController.saveGame(gameToBeSaved).getBody();

        Assertions.assertThat(gameSaved).isNotNull();

        Assertions.assertThat(gameSaved.getContent().getId()).isNotNull();
    }

    @Test
    @DisplayName("saveGame throws CompanyNotFound when company is not found")
    void saveGame_ThrowsCompanyNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.when(gameService.saveGame(ArgumentMatchers.any(GameRequestDTO.class)))
            .thenThrow(CompanyNotFoundException.class);
        
        Assertions.assertThatExceptionOfType(CompanyNotFoundException.class)
            .isThrownBy(() -> gameController.saveGame(GameDTOCreator.createGameRequest()));
        
    }

    @Test
    @DisplayName("updateGameById returns EntityModel of GameResponse when successful")
    void updateGameById_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        GameRequestDTO gameToBeUpdated = GameDTOCreator.createAlteredGameRequest();
        
        Long idExpected = 1l;

        EntityModel<GameResponseDTO> alteredGame = gameController.updateGameById(1l, gameToBeUpdated).getBody();

        Assertions.assertThat(alteredGame).isNotNull();

        Assertions. assertThat(alteredGame.getContent().getId()).isEqualTo(idExpected);
    }

    @Test
    @DisplayName("updateGameById thrown NotFoundException when game is not found")
    void updateGameById_ThrowsNotFoundException_WhenGameIsNotFound() {
        BDDMockito.when(gameService.updateGameById(ArgumentMatchers.anyLong(), 
            ArgumentMatchers.any(GameRequestDTO.class))).thenThrow(NotFoundException.class);
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> gameController.updateGameById(1l,GameDTOCreator.createGameRequest()));
    }

    
    @Test
    @DisplayName("deleteGameById removes Game when successful")
    void deleteGameById_DeletesGame_WhenSuccessful() {
        Assertions.assertThatCode(() -> gameController.deleteGameById(1l))
            .doesNotThrowAnyException();
        
        ResponseEntity<Void> entity = gameController.deleteGameById(1l);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteGameById throws NotFoundException when game is not found")
    void deleteGameById_ThrowsNotFoundException_WhenGameIsNotFound() {
        BDDMockito.doThrow(EntityNotFoundException.class)
            .when(gameService).deleteGameById(ArgumentMatchers.anyLong());
        
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> gameController.deleteGameById(1l));
    }

}
