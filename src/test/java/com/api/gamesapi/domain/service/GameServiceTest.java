package com.api.gamesapi.domain.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.api.gamesapi.api.mapper.GameMapper;
import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.domain.repository.GameRepository;
import com.api.gamesapi.util.CompanyCreator;
import com.api.gamesapi.util.GameCreator;
import com.api.gamesapi.util.GameDTOCreator;

@ExtendWith(SpringExtension.class)
public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;
    
    @Mock
    private CompanyRepository companyRepository;
    
    @Mock
    private GameMapper gameMapper;

    @BeforeEach
    void setup() {
        Page<Game> page = new PageImpl<>(List.of(GameCreator.createGameToBeSaved()));
        PagedModel<EntityModel<GameResponseDTO>> pagedModel = GameDTOCreator.createPagedModelGameResponse();
        BDDMockito.when(gameRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(page);
        BDDMockito.when(gameMapper.toPagedModel(ArgumentMatchers.any(Page.class)))
            .thenReturn(pagedModel);
        BDDMockito.when(gameRepository.findByCompanyId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(page);
        BDDMockito.when(companyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(
            Optional.of(CompanyCreator.createValidCompany())
        );
        BDDMockito.when(gameRepository.save(ArgumentMatchers.any(Game.class))).thenReturn(
            GameCreator.createValidGame()
        );
        BDDMockito.when(gameMapper.toEntity(ArgumentMatchers.any(GameRequestDTO.class)))
            .thenReturn(GameCreator.createValidGame());
        BDDMockito.when(gameMapper.toModelResponse(ArgumentMatchers.any(Game.class)))
            .thenReturn(GameDTOCreator.createEntityModelGameResponse());
        BDDMockito.when(gameRepository.existsById(ArgumentMatchers.anyLong()))
            .thenReturn(true);
        BDDMockito.when(gameRepository.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(GameCreator.createValidGame()));
    }

    @Test
    @DisplayName("getAllGames returns PagedModel of GameResponse when successful")
    void getAllGames_ReturnsPagedModelOfGameResponse_WhenSuccessful() {
        String expectedName = GameDTOCreator.createGameResponse().getName();
        
        PagedModel<EntityModel<GameResponseDTO>> page = gameService.getAllGames(PageRequest.of(0, 1));
        
        Assertions.assertThat(page.getContent()).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(page.getContent().stream().toList().get(0).getContent().getName())
            .isEqualTo(expectedName);

    }

    @Test
    @DisplayName("getAllGames returns empty PagedModel of GameResponse when no game is found")
    void getAllGames_ReturnsEmptyPagedModelOfGameResponse_WhenSuccessful() {
        BDDMockito.when(gameRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(Page.empty());
         BDDMockito.when(gameMapper.toPagedModel(ArgumentMatchers.any(Page.class)))
            .thenReturn(PagedModel.empty());
        
        PagedModel<EntityModel<GameResponseDTO>> page = gameService.getAllGames(PageRequest.of(0, 1));
        
        Assertions.assertThat(page.getContent()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("listGamesByCompanyId returns PagedModel of GameResponse when successful")
    void listGamesByCompanyId_ReturnsPagedModelOfGameResponse_WhenSuccessful() {
        Company company = CompanyCreator.createValidCompany();
        
        PagedModel<EntityModel<GameResponseDTO>> games = gameService.listGamesByCompanyId(company.getId(), PageRequest.of(0, 1));

        Assertions.assertThat(games.getContent()).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(games.getContent().stream().toList().get(0).getContent().getCompanyName())
            .isEqualTo(company.getName());
    }

    @Test
    @DisplayName("listGamesByCompanyId returns empty PagedModel of GameResponse when no game is found")
    void listGamesByCompanyId_ReturnsEmptyPagedModelOfGameResponse_WhenNoGameIsFound() {
        BDDMockito.when(gameRepository.findByCompanyId(ArgumentMatchers.anyLong(),ArgumentMatchers.any(PageRequest.class)))
            .thenReturn(Page.empty());
         BDDMockito.when(gameMapper.toPagedModel(ArgumentMatchers.any(Page.class)))
            .thenReturn(PagedModel.empty());
        
        PagedModel<EntityModel<GameResponseDTO>> games = gameService.listGamesByCompanyId(1l, PageRequest.of(0, 1));

        Assertions.assertThat(games.getContent()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("saveGame returns EntityModel of GameResponse when successful")
    void saveGame_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        GameRequestDTO gameToBeSaved = GameDTOCreator.createGameRequest();

        EntityModel<GameResponseDTO> savedGame = gameService.saveGame(gameToBeSaved);

        Assertions.assertThat(savedGame).isNotNull();
        
        Assertions.assertThat(savedGame.getContent().getId()).isNotNull();
        
        Assertions.assertThat(savedGame.getContent().getCompanyName()).isNotNull();
    }
    @Test
    @DisplayName("saveGame throws NotFoundException when Company of the Game is not found")
    void saveGame_ThrowsNotFoundException_WhenCompanyOfTheGameIsNotFound() {
        BDDMockito.when(companyRepository.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.empty());
        
        Assertions.assertThatExceptionOfType(CompanyNotFoundException.class)
            .isThrownBy(() -> gameService.saveGame(GameDTOCreator.createGameRequest()))
            .withMessageContaining("Company not found with id");
    }

    @Test
    @DisplayName("deleteGameById removes Game when successful ")
    void deleteGameById_RemovesGame_WhenSuccessful() {
        Assertions.assertThatCode(() -> gameService.deleteGameById(1l))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("deleteGameById throws NotFoundException when game is not found")
    void deleteGameById_ThrowsNotFoundException_WhenGameIsNotFound() {
        BDDMockito.when(gameRepository.existsById(ArgumentMatchers.anyLong()))
            .thenReturn(false);
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> gameService.deleteGameById(1l))
            .withMessageContaining("Game not found with id");
    }


    @Test
    @DisplayName("searchGameById returns EntityModel of GameResponse when successful")
    void searchGameById_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        Long idExpected = GameCreator.createValidGame().getId();
        
        EntityModel<GameResponseDTO> game = gameService.searchGameById(1l);
        
        Assertions.assertThat(game).isNotNull();

        Assertions.assertThat(game.getContent().getId()).isNotNull().isEqualTo(idExpected);
    }

    @Test
    @DisplayName("searchGameById throws NotFoundException when game is not found")
    void searchGameById_ThrowsNotFoundException_WhenGameIsNotFound() {
        BDDMockito.when(gameRepository.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.empty());
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> gameService.searchGameById(1l))
            .withMessageContaining("Game not found with id"); 
    }

    @Test
    @DisplayName("updateGameById returns EntityModel of GameResponse when successful")
    void updateGameById_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        Long idExpected = GameCreator.createValidGame().getId();
        
        GameRequestDTO gameTobeUpdated = GameDTOCreator.createAlteredGameRequest();
        
        EntityModel<GameResponseDTO> alteredGame = gameService.updateGameById(idExpected, gameTobeUpdated);

        Assertions.assertThat(alteredGame).isNotNull();

        Assertions.assertThat(alteredGame.getContent().getId()).isEqualTo(idExpected);
    }

    @Test
    @DisplayName("updateGameById throws CompanyNotFoundException when the Company of the Game is not found")
    void updateGameById_ThrowsCompanyNotFoundException_WhenTheCompanyOfTheGameIsNotFound() {
        BDDMockito.when(companyRepository.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.empty());

        GameRequestDTO gameTobeUpdated = GameDTOCreator.createAlteredGameRequest();
        
        Assertions.assertThatExceptionOfType(CompanyNotFoundException.class)
            .isThrownBy(() -> gameService.updateGameById(1l, gameTobeUpdated))
            .withMessageContaining("Company not found with id");
    }

    @Test
    @DisplayName("updateGameById throws NotFoundException when game is not found")
    void updateGameById_ThrowsNotFoundException_WhenGameIsNotFound() {
        BDDMockito.when(gameRepository.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.empty());

        GameRequestDTO gameTobeUpdated = GameDTOCreator.createAlteredGameRequest();
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> gameService.updateGameById(1l, gameTobeUpdated))
            .withMessageContaining("Game not found with id");
    }


    @Test
    @DisplayName("gameExists returns true when successful")
    void gameExists_ReturnsTrue_WhenSuccessful() {
        Game game = GameCreator.createValidGame();

        Boolean gameExists = gameService.gameExists(game.getId());

        Assertions.assertThat(gameExists).isTrue();
    }

    @Test
    @DisplayName("gameExists throws NotFoundException when Game is not found")
    void gameExists_ThrowsNotFoundException_WhenGameIsNotFound() {
        BDDMockito.when(gameRepository.existsById(ArgumentMatchers.anyLong()))
            .thenReturn(false);

        Assertions.assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> gameService.gameExists(1l))
            .withMessageContaining("Game not found with id");
    }

}
