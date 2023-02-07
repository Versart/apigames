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
    public ResponseEntity<Game> getGameById(@PathVariable long gameId){
        return gameService.searchGameById(gameId).map(
                game -> {
                    return ResponseEntity.ok(game);
                }
        ).orElse(
                ResponseEntity.notFound().build()
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Game saveGame(@Valid @RequestBody Game game) {
        return gameService.saveGame(game);
    }

    @PutMapping("/{gameId}")
    public ResponseEntity<Game> updateGameById(@PathVariable long gameId, @Valid @RequestBody Game game) {
        return gameService.searchGameById(gameId).map(
                gameOld -> {
                    game.setId(gameOld.getId());
                    return ResponseEntity.ok(gameService.saveGame(game));
                }
        ).orElse(ResponseEntity.notFound().build());
    }

   @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGameById(@PathVariable long gameId) {
        Optional<Game> gameEncontrado = gameService.searchGameById(gameId);
        if(gameEncontrado.isPresent()){
            gameService.deleteGameById(gameId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
   }

}
