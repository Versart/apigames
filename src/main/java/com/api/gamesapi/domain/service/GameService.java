package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.GameMapper;
import com.api.gamesapi.api.model.GameDTO;
import com.api.gamesapi.domain.repository.GameRepository;
import com.api.gamesapi.domain.model.Game;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;


    public List<GameDTO> listGames(){

        return gameMapper.toModelList(gameRepository.findAll());
    }

    @Transactional
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Transactional
    public void deleteGameById(long gameId) {
        gameRepository.deleteById(gameId);
    }

    public Optional<Game> searchGameById(Long gameId) {
        return gameRepository.findById(gameId);
    }


}
