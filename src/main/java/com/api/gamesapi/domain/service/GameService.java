package com.api.gamesapi.domain.service;

import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;


    public List<Game> listGames(){
        return gameRepository.findAll();
    }

    @Transactional
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

}
