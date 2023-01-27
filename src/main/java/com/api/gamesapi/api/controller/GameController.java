package com.api.gamesapi.api.controller;

import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.service.GameService;
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
    public List<Game> listGames() {
        return gameService.listGames();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Game saveGame(@RequestBody Game game) {
        return gameService.saveGame(game);
    }

}
