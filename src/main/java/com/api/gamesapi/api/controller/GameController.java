package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<GameResponseDTO> listGames() {
        return gameService.listGames();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponseDTO> getGameById(@PathVariable long gameId){
        return ResponseEntity.ok(gameService.searchGameById(gameId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GameResponseDTO saveGame(@Valid @RequestBody GameRequestDTO game) {

        return gameService.saveGame(game);
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<GameResponseDTO> updateGameById(@PathVariable long gameId, @Valid @RequestBody GameRequestDTO game) {
        return ResponseEntity.ok(gameService.updateGameById(gameId,game));
    }

   @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGameById(@PathVariable long gameId) {

        if(gameService.gameExists(gameId)){
            gameService.deleteGameById(gameId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
   }

}
