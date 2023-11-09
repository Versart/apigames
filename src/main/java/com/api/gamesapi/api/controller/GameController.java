package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.GameService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class GameController {

    Logger logger = LogManager.getLogger(GameController.class);

    private final GameService gameService;

    @GetMapping
    @Operation(summary = "Lists all games paginated", tags = "Game", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200")
    })
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> getAllGames(Pageable pageable) { 
        logger.info("Received request to fetch all games");
        return ResponseEntity.ok(gameService.getAllGames(pageable));
    }

    @GetMapping("/{gameId}")
    @Operation(summary = "Gets a game by id", tags = "Game", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the game does not exists in the database", responseCode = "404")
    })
    public ResponseEntity<EntityModel<GameResponseDTO>> getGameById(@PathVariable long gameId){
        logger.info("Received request to fetch game with id {}", gameId);
        return ResponseEntity.ok(gameService.searchGameById(gameId));
    }

    @PostMapping
    @Operation(summary = "Creates a new game", tags = "Game", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "201"),
        @ApiResponse(description = "When the body is invalid", responseCode = "400")
    })
    public ResponseEntity<EntityModel<GameResponseDTO>> saveGame(@Valid @RequestBody GameRequestDTO game) {
        logger.info("Received request to create new game");
        return new ResponseEntity<>(gameService.saveGame(game),HttpStatus.CREATED);
    }

    @PutMapping("/{gameId}")
    @Operation(summary = "Alters a game", tags = "Game", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the game does not exists in the database", responseCode = "404")
    })
    public ResponseEntity<EntityModel<GameResponseDTO>> updateGameById(@PathVariable long gameId, @Valid @RequestBody GameRequestDTO game) {
        logger.info("Received request to update game with id {}", gameId);
        return ResponseEntity.ok(gameService.updateGameById(gameId,game));
    }

   @DeleteMapping("/{gameId}")
   @Operation(summary = "Deletes a game by id", tags = "Game", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "204"),
        @ApiResponse(description = "When the game does not exists in the database", responseCode = "404")
   })
    public ResponseEntity<Void> deleteGameById(@PathVariable long gameId) {
        logger.info("Received request to delete game with id {}", gameId);
        gameService.deleteGameById(gameId);
        return ResponseEntity.noContent().build();
   }

}
