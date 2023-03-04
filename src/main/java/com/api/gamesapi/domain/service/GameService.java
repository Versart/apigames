package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.controller.GameController;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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

    public CollectionModel<EntityModel<GameResponseDTO>> listGames() {
        List<EntityModel<GameResponseDTO>> gamesModel;

        gamesModel = gameRepository.findAll().stream().map(
                game -> EntityModel.of(gameMapper.toModelResponse(game),
                        linkTo(methodOn(GameController.class).getGameById(game.getId())).withSelfRel(),
                        linkTo(methodOn(GameController.class).listGames()).withRel("games"))
        ).collect(Collectors.toList());
        return CollectionModel.of(gamesModel,linkTo(methodOn(GameController.class).listGames()).withSelfRel());
    }

    @Transactional
    public GameResponseDTO saveGame(GameRequestDTO gameRequestDTO) {
        return companyRepository.findById(gameRequestDTO.getCompanyId()).map(
                company -> {
                    Game gameSaved = gameRepository.save(gameMapper.toEntity(gameRequestDTO));
                    return gameMapper.toModelResponse(gameSaved);
                }
        ).orElseThrow(() -> new CompanyNotFoundException("Company not found!"));
    }

    @Transactional
    public void deleteGameById(long gameId) {
        gameRepository.deleteById(gameId);
    }

    public EntityModel<GameResponseDTO> searchGameById(Long gameId) {

        return gameRepository.findById(gameId).map(
                game ->
                        EntityModel.of(gameMapper.toModelResponse(game),
                                linkTo(methodOn(GameController.class).getGameById(gameId)).withSelfRel(),
                                linkTo(methodOn(GameController.class).listGames()).withRel("games")
                        )
        ).orElseThrow(() -> new NotFoundException("Game not found!"));
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
        if (gameRepository.existsById(gameId))
            return true;
        throw new NotFoundException("Game not found!");
    }

}
