package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.GameMapper;
import com.api.gamesapi.api.model.GameDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.domain.repository.GameRepository;
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
    private CompanyRepository companyRepository;

    @Autowired
    private GameMapper gameMapper;


    public List<GameDTO> listGames(){

        return gameMapper.toModelList(gameRepository.findAll());
    }

    @Transactional
    public GameDTO saveGame(Game game) {
       return companyRepository.findById(game.getCompany().getId()).map(
               company ->{
                   game.setCompany(company);
                   gameRepository.save(game);
                   return  gameMapper.toModel(game);
               }
       ).orElseThrow( () -> new CompanyNotFoundException("Company not found!"));
    }

    @Transactional
    public void deleteGameById(long gameId) {
        gameRepository.deleteById(gameId);
    }

    public GameDTO searchGameById(Long gameId) {
        return gameRepository.findById(gameId).map(
                game -> gameMapper.toModel(game)
        ).orElseThrow( () -> new NotFoundException("Game not found!"));
    }

    public GameDTO updateGameById(Long gameId, Game game) {

        return gameRepository.findById(gameId).map(
                gameUpdate -> {
                    game.setId(gameUpdate.getId());
                    return gameMapper.toModel(gameRepository.save(game));
                }
        ).orElseThrow(() -> new NotFoundException("Company not found!"));
    }

    public boolean gameExists(Long gameId) {
        if(gameRepository.existsById(gameId))
            return true;
        throw new NotFoundException("Game not found!");
    }

}
