package com.api.gamesapi.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.domain.repository.GameRepository;
import com.api.gamesapi.domain.repository.UserRepository;
import com.api.gamesapi.util.CompanyCreator;
import com.api.gamesapi.util.GameCreator;
import com.api.gamesapi.util.GameDTOCreator;
import com.api.gamesapi.util.UserCreator;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class GameControllerIT {
    
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("getAllGames returns PagedModel of GameResponse when successful")
    void getAllGames_ReturnsPagedModelOfGameResponse_Whensuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company company = companyRepository.findAll().get(0);
        Game gameToBeSaved = GameCreator.createGameToBeSaved();
        gameToBeSaved.setCompany(company);
        Game gameSaved = gameRepository.save(gameToBeSaved);
        String url = "/games";
        
        ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> exchange = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null,httpHeaders), new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(exchange.getBody()).isNotNull().isNotEmpty();

        Assertions.assertThat(exchange.getBody().getContent()).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(exchange.getBody().getContent().stream().toList()
            .get(0).getContent().getId()).isEqualTo(gameSaved.getId());
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList().get(0).getLinks())
            .isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList().get(0)
            .getLinks().toString()).contains(url + "/" + gameSaved.getId());
        
        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("getGameById returns EntityModel of GameResponse when successful")
    void getGameById_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company company = companyRepository.findAll().get(0);
        Game gameToBeSaved = GameCreator.createGameToBeSaved();
        gameToBeSaved.setCompany(company);
        Game gameSaved = gameRepository.save(gameToBeSaved);
        String url = "/games/{id}";
        Long idGameExpected = gameSaved.getId();

        ResponseEntity<EntityModel<GameResponseDTO>> exchange = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null,httpHeaders), new ParameterizedTypeReference<>() {
        }, idGameExpected);

        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(exchange.getBody().getContent()).isNotNull();

        Assertions.assertThat(exchange.getBody().getContent().getId()).isEqualTo(idGameExpected);

        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();

        Assertions.assertThat(exchange.getBody().getLinks().toString()).contains(
            url.replace("/{id}","") + "/" + idGameExpected
        );
    }

    @Test
    @DisplayName("saveGame returns EntityModel of GameResponse when successful")
    void saveGame_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company company = companyRepository.findAll().get(0);
        GameRequestDTO gameToBeSaved = GameDTOCreator.createGameRequest();
        gameToBeSaved.setCompanyId(company.getId());
        String url = "/games";

        ResponseEntity<EntityModel<GameResponseDTO>> exchange = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(gameToBeSaved,httpHeaders), new ParameterizedTypeReference<>() { 
        });

        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(exchange.getBody().getContent()).isNotNull();
        
        Assertions.assertThat(exchange.getBody().getContent().getId()).isNotNull();

        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();
        
    }

    @Test
    @DisplayName("updateGameById returns EntityModel of GameResponse when successful")
    void updateGameById_ReturnsEntityModelOfGameResponse_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company company = companyRepository.findAll().get(0);
        Game gameTobeSaved = GameCreator.createGameToBeSaved();
        gameTobeSaved.setCompany(company);
        Game gameSaved = gameRepository.save(gameTobeSaved);
        Long idUpdated = gameSaved.getId();
        GameRequestDTO gameToBeUpdated = GameDTOCreator.createAlteredGameRequest();
        gameToBeUpdated.setCompanyId(company.getId());
        String url = "/games/{id}";

        ResponseEntity<EntityModel<GameResponseDTO>> exchange = testRestTemplate.exchange(url, HttpMethod.PUT, 
            new HttpEntity<>(gameToBeUpdated,httpHeaders), new ParameterizedTypeReference<>() {    
            },idUpdated);
        
        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(exchange.getBody().getContent().getName()).isEqualTo(gameToBeUpdated.getName());

        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();

        Assertions.assertThat(exchange.getBody().getLinks().toString())
            .contains(url.replace("/{id}", "") + "/" + idUpdated);
    }

    @Test
    @DisplayName("deleteGameById removes Game when successful")
    void deleteGameById_DeleteGame_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company company = companyRepository.findAll().get(0);
        Game gameTobeSaved = GameCreator.createGameToBeSaved();
        gameTobeSaved.setCompany(company);
        Game gameSaved = gameRepository.save(gameTobeSaved);
        Long idDeleted = gameSaved.getId();
        String url = "/games/{id}";

        ResponseEntity<Void> exchange = testRestTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(null,httpHeaders), new ParameterizedTypeReference<>() {    
        },idDeleted);

        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @BeforeEach
    void setup() {
        userRepository.save(UserCreator.createUserAdmin());
        companyRepository.save(CompanyCreator.createCompanyToBeSaved());
    }

    @AfterEach
    void end() {
        gameRepository.deleteAll();
        companyRepository.deleteAll();
        userRepository.deleteAll();
    }
}
