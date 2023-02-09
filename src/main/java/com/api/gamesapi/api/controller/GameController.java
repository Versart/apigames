package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.GameDTO;
import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<GameDTO> listGames() {
        return gameService.listGames();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable long gameId){
        return ResponseEntity.ok(gameService.searchGameById(gameId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GameDTO saveGame(@Valid @RequestBody Game game) {
        return gameService.saveGame(game);
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<GameDTO> updateGameById(@PathVariable long gameId, @Valid @RequestBody Game game) {
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
