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
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    Logger logger = LogManager.getLogger(GameService.class);

    private final GameRepository gameRepository;


    private final CompanyRepository companyRepository;


    private final GameMapper gameMapper;



    public PagedModel<EntityModel<GameResponseDTO>> getAllGames(Pageable pageable) {
        logger.info("Fetching all games");
        return gameMapper.toPagedModel(gameRepository.findAll(pageable));
    }


    public PagedModel<EntityModel<GameResponseDTO>> listGamesByCompanyId(Long companyId, Pageable pageable){
        logger.info("Fetching all games of the company with id {}", companyId);
        return gameMapper.toPagedModel(gameRepository.findByCompanyId(companyId, pageable));
    }

    @Transactional
    public EntityModel<GameResponseDTO> saveGame(GameRequestDTO gameRequestDTO) {
        logger.info("Creating new game");
        return companyRepository.findById(gameRequestDTO.getCompanyId()).map(
                company -> {
                    Game gameSaved = gameRepository.save(gameMapper.toEntity(gameRequestDTO));
                    return gameMapper.toModelResponse(gameSaved);
                }
        ).orElseThrow(() -> new CompanyNotFoundException("Company not found!"));
    }

    @Transactional
    public void deleteGameById(long gameId) {
        logger.info("Deleting game with id {}", gameId);
        if(gameExists(gameId)){
            gameRepository.deleteById(gameId);
        }
    }

    public EntityModel<GameResponseDTO> searchGameById(Long gameId) {
        logger.info("Fetching game with id {}", gameId);
        return gameRepository.findById(gameId).map(
                       gameMapper::toModelResponse
        ).orElseThrow(() -> new NotFoundException("Game not found!"));
    }

    public EntityModel<GameResponseDTO> updateGameById(Long gameId, GameRequestDTO gameRequestDTO) {
        logger.info("Updating game with id {}", gameId);
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

    public boolean gameExists(Long gameId) throws NotFoundException{
        logger.info("Verifying if exists game with id {}", gameId);
        if (gameRepository.existsById(gameId))
            return true;
        throw new NotFoundException("Game not found!");
    }

}
