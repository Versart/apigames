package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.GameMapper;
import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.domain.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private CompanyService companyService;

    public List<GameResponseDTO> listGames(){

        return gameMapper.toModelResponseList(gameRepository.findAll());
    }

    @Transactional
    public GameResponseDTO saveGame(GameRequestDTO gameRequestDTO) {
       return companyRepository.findById(gameRequestDTO.getCompanyId()).map(
                company ->  {
                  Game gameSaved = gameRepository.save(gameMapper.toEntity(gameRequestDTO));
                  return gameMapper.toModelResponse(gameSaved);
               }
       ).orElseThrow( () -> new CompanyNotFoundException("Company not found!"));
    }

    @Transactional
    public void deleteGameById(long gameId) {
        gameRepository.deleteById(gameId);
    }

    public GameResponseDTO searchGameById(Long gameId) {
        return gameRepository.findById(gameId).map(
                game -> gameMapper.toModelResponse(game)
        ).orElseThrow( () -> new NotFoundException("Game not found!"));
    }

    public GameResponseDTO updateGameById(Long gameId, GameRequestDTO gameRequestDTO) {
        return gameMapper.toModelResponse(gameRepository.findById(gameId).map(
                gameUpdate -> {
                    companyRepository.findById(gameRequestDTO.getCompanyId()).map(
                           company -> {
                               gameUpdate.setCategory(gameRequestDTO.getCategory());
                               gameUpdate.setName(gameRequestDTO.getName());
                               gameUpdate.setCompany(company);
                               return gameMapper.toModelResponse(gameRepository.save(gameUpdate));
                           }
                   ).orElseThrow(() -> new CompanyNotFoundException("Company not found!"));
                    return gameUpdate;
                }
        ).orElseThrow(() -> new NotFoundException("Game not found!")));

    }

    public boolean gameExists(Long gameId) {
        if(gameRepository.existsById(gameId))
            return true;
        throw new NotFoundException("Game not found!");
    }

}
