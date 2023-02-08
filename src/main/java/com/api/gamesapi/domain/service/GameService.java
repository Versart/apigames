package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.GameMapper;
import com.api.gamesapi.api.model.GameDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
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

    public Optional<Game> searchGameById(Long gameId) {
        return gameRepository.findById(gameId);
    }


}
