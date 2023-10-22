package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.GameService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> getAllGames(Pageable pageable) {
        return ResponseEntity.ok(gameService.getAllGames(pageable));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<EntityModel<GameResponseDTO>> getGameById(@PathVariable long gameId){
        return ResponseEntity.ok(gameService.searchGameById(gameId));
    }

    @PostMapping
    public ResponseEntity<EntityModel<GameResponseDTO>> saveGame(@Valid @RequestBody GameRequestDTO game) {
        return new ResponseEntity<>(gameService.saveGame(game),HttpStatus.CREATED);
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<EntityModel<GameResponseDTO>> updateGameById(@PathVariable long gameId, @Valid @RequestBody GameRequestDTO game) {
        return ResponseEntity.ok(gameService.updateGameById(gameId,game));
    }

   @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGameById(@PathVariable long gameId) {
        gameService.deleteGameById(gameId);
        return ResponseEntity.noContent().build();
   }

}
