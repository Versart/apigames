package com.api.gamesapi.domain.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.util.CompanyCreator;
import com.api.gamesapi.util.GameCreator;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("existsById returns true when game exists")
    void existsById_ReturnsTrue_WhenGameExists() {
        Game gameToBeSaved = GameCreator.createGameToBeSaved();

        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        
        gameToBeSaved.setCompany(companySaved);

        Game gameSaved = gameRepository.save(gameToBeSaved);

        Long idExpected = gameSaved.getId();
        
        Boolean gameExists = gameRepository.existsById(idExpected);

        Assertions.assertThat(gameExists).isTrue();

    }

    @Test
    @DisplayName("existsById returns false when game does not exists")
    void existsById_ReturnsFalse_WhenGameDoesNotExists() {
        Long idExpected = 1l;
        
        Boolean gameExists = gameRepository.existsById(idExpected);

        Assertions.assertThat(gameExists).isFalse();

    }

    @Test
    @DisplayName("findByCompanyId returns Page of Game when successful")
    void findByCompanyId_ReturnsPageOfGame_WhenSuccessful() {
        Game gameToBeSaved = GameCreator.createGameToBeSaved();

        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        
        gameToBeSaved.setCompany(companySaved);

        Game gameSaved = gameRepository.save(gameToBeSaved);

        Long idexpected = companySaved.getId();

        Page<Game> pageGame = gameRepository.findByCompanyId(idexpected, PageRequest.of(0, 10));
        
        Assertions.assertThat(pageGame).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(pageGame.getContent().get(0))
            .isEqualTo(gameSaved);
    }

    @Test
    @DisplayName("findByCompanyId returns empty Page of Game when game is not found")
    void findByCompanyId_ReturnsEmptyPageOfGame_WhenGameIsNotFound() {
        Long idexpected = 1l;

        Page<Game> pageGame = gameRepository.findByCompanyId(idexpected, PageRequest.of(0, 10));
        
        Assertions.assertThat(pageGame).isNotNull().isEmpty();

    }

    @AfterEach
    void end() {
        gameRepository.deleteAll();
        companyRepository.deleteAll();
    }
}
