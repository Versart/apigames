package com.api.gamesapi.integration;

import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.model.Game;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.domain.repository.GameRepository;
import com.api.gamesapi.domain.repository.UserRepository;
import com.api.gamesapi.util.CompanyCreator;
import com.api.gamesapi.util.CompanyDTOCreator;
import com.api.gamesapi.util.GameCreator;
import com.api.gamesapi.util.UserCreator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CompanyControllerIT {


    @Autowired
    private TestRestTemplate testRestTemplate;


    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private UserRepository userRepository;
    

    @Test
    @DisplayName("listCompanies returns PagedModel of CompanyResponse when successful")
    void listCompanies_ReturnsPageModelOfEntityModelCompanyDTO_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        String url = "/companies";

        ResponseEntity<PagedModel<EntityModel<CompanyResponse>>>
                exchange = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(
                        null,httpHeaders
                ),
                new ParameterizedTypeReference<>(){
                });

        Assertions.assertThat(exchange).isNotNull();

        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        Assertions.assertThat(exchange.getBody()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent().stream().map(
                company -> company.getContent().getId()
        )).contains(companySaved.getId());
        
        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList()
                .get(0).getLinks()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList()
                .get(0).getLinks().toString()).contains("/companies/" + companySaved.getId());
    }

    @Test
    @DisplayName("getCompanyById returns model of companyDTO when successful")
    void getCompanyById_ReturnEntityModelOfCompanyDto_WhenSuccessfull() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        Long idCompanyExpected = companySaved.getId();
        String url = "/companies/{id}";
        ResponseEntity<EntityModel<CompanyResponse>>
                exchange = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(
                        null,httpHeaders
                )
                , new ParameterizedTypeReference<>() {
                }, idCompanyExpected);

        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(exchange.getBody()).isNotNull();

        Assertions.assertThat(exchange.getBody().getContent()).isNotNull();

        Assertions.assertThat(exchange.getBody().getContent().getId()).isEqualTo(idCompanyExpected);
        
        Assertions.assertThat(exchange.getBody().getLinks()).isNotEmpty().isNotNull();
        
        Assertions.assertThat(exchange.getBody().getLinks().toString()).contains(url.replace("/{id}","") + "/"+ idCompanyExpected);
    }

    @Test
    @DisplayName("GetByName returns a collection model of companyDTO when successful")
    void getByName_ReturnCollectionModelOfCompanyDto_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        String nameExpected = companySaved.getName();
        String url = String.format("/companies/find?name=%s", nameExpected);
        ResponseEntity<CollectionModel<EntityModel<CompanyRequest>>> exchange = testRestTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(null,httpHeaders),
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertThat(exchange).isNotNull();
        
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        Assertions.assertThat(exchange.getBody()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList().get(0).getContent()
                .getName()).isEqualTo(nameExpected);
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList().get(0)
                .getLinks()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList()
                .get(0).getLinks().toString()).contains("/companies/" + companySaved.getId());
    }

    @Test
    @DisplayName("getByName returns an empty collection model of companyDTO when company is not found")
    void getByName_ReturnEmptyCollectionModelOfCompanyDto_WhenCompanyIsNotFound() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        String nameExpected = "";
        String url = String.format("/companies/find?name=%s", nameExpected);
        ResponseEntity<CollectionModel<EntityModel<CompanyRequest>>> exchange = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null,httpHeaders), new ParameterizedTypeReference<>() {
        });

        Assertions.assertThat(exchange).isNotNull();
        
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        Assertions.assertThat(exchange.getBody()).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("saveCompany returns an entity model of companyDTO when successful")
    void saveCompany_ReturnEntityModelOfCompanyDTO_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        CompanyRequest companyToBeSaved = CompanyDTOCreator.createCompanyRequest();
        String url = "/companies";
        ResponseEntity<EntityModel<CompanyResponse>> exchange = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(companyToBeSaved,httpHeaders),
                new ParameterizedTypeReference<>() {
                });

        Assertions.assertThat(exchange).isNotNull();
        
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        Assertions.assertThat(exchange.getBody()).isNotNull();
        
        Assertions.assertThat(exchange.getBody().getContent()).isNotNull();
        
        Assertions.assertThat(exchange.getBody().getContent().getId()).isNotNull();
        
        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();

    }

    @Test
    @DisplayName("UpdateCompanyById returns a altered companyDTO")
    void updateCompanyById_ReturnEntityModelOfCompanyDto_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        String url = "/companies/{id}";
        CompanyRequest companyToUpdated = CompanyDTOCreator.createCompanyRequest();
        Long idUpdated = companySaved.getId();
        ResponseEntity<EntityModel<CompanyRequest>> exchange = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(companyToUpdated,httpHeaders),
                new ParameterizedTypeReference<>() {
                }, idUpdated);

        Assertions.assertThat(exchange).isNotNull();
        
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        Assertions.assertThat(exchange.getBody()).isNotNull();
        
        Assertions.assertThat(exchange.getBody().getContent().getName()).isEqualTo(companyToUpdated.getName());
        
        Assertions.assertThat(exchange.getBody().getContent().getDateOfFoundation()).isEqualTo(companyToUpdated.getDateOfFoundation());
        
        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getLinks().toString()).contains("/companies/" + idUpdated);
    }

    @Test
    void getGamesByCompanyId_ReturnCollectionEntityModelOfGamesResponse_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        Game gameToSaved = GameCreator.createGameToBeSaved();
        gameToSaved.setCompany(companySaved);
        Game gameSaved = gameRepository.save(gameToSaved);
        String url = "/companies/{id}/games";
        Long idCompany = companySaved.getId();

        ResponseEntity<CollectionModel<EntityModel<GameResponseDTO>>> exchange = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null,httpHeaders),
                new ParameterizedTypeReference<>() {
                }, idCompany);
        
        Assertions.assertThat(exchange).isNotNull();
        
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        Assertions.assertThat(exchange.getBody()).isNotNull();
        
        Assertions.assertThat(exchange.getBody().getContent()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList().get(0).getContent()).isNotNull();
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList().get(0).getContent().getName()
            ).isEqualTo(gameSaved.getName());
        
        Assertions.assertThat(exchange.getBody().getContent().stream().toList().get(0).getLinks()).isNotNull();
        
        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("deleteCompanyById removes companyDTO when successful")
    void deleteCompanyById_DeleteCompany_WhenSuccessful() {
        LoginRequest loginRequest = new LoginRequest("maria","12345678");
        String token = testRestTemplate.postForObject("/auth/login",loginRequest,String.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer " + token);

        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        String url = "/companies/{id}";
        Long idToDelete = companySaved.getId();

        ResponseEntity<Void> exchange = testRestTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(null,httpHeaders), Void.class, idToDelete);

        Assertions.assertThat(exchange).isNotNull();
        
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @BeforeEach
    void setup(){
        companyRepository.deleteAll();
        userRepository.save(UserCreator.createUserAdmin());
    }

    @AfterEach
    void end(){
        companyRepository.deleteAll();
        userRepository.deleteAll();
    }

}
