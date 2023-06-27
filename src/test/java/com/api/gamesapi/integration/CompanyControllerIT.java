package com.api.gamesapi.integration;

import com.api.gamesapi.api.controller.CompanyController;
import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.util.CompanyCreator;
import com.api.gamesapi.util.CompanyDTOCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanyControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("listCompanies returns paged of entity model companyDTo when successful")
    void listCompanies_ReturnsPageModelOfEntityModelCompanyDTO_WhenSuccessful() {
        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        ResponseEntity<PagedModel<EntityModel<CompanyDTO>>>
                exchange = testRestTemplate.exchange("/companies", HttpMethod.GET, null,
                new ParameterizedTypeReference<PagedModel<EntityModel<CompanyDTO>>>() {
                });
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getBody()).isNotNull().isNotEmpty();
        Assertions.assertThat(exchange.getBody().getContent().contains(companySaved));
    }

    @Test
    @DisplayName("getCompanyById returns model of companyDTO when successful")
    void getCompanyById_ReturnEntityModelOfCompanyDto_WhenSuccessfull() {
        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        Long idCompanyExpected = companySaved.getId();
        String url = "/companies/{id}";
        ResponseEntity<EntityModel<CompanyDTO>>
                exchange = testRestTemplate.exchange(url, HttpMethod.GET, null
                , new ParameterizedTypeReference<EntityModel<CompanyDTO>>() {
                }, idCompanyExpected);

        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getBody()).isNotNull();
        Assertions.assertThat(exchange.getBody().getLinks()).isNotEmpty().isNotNull();
        Assertions.assertThat(exchange.getBody().getLinks().toString()).contains("/companies/" + idCompanyExpected);
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GetByName returns a collection model of companyDTO when successful")
    void getByName_ReturnCollectionModelOfCompanyDto_WhenSuccessful() {
        String nameExpected = "Companhia Teste X";
        String url = String.format("/companies/find?name=%s", nameExpected);
        ResponseEntity<CollectionModel<EntityModel<CompanyDTO>>> exchange = testRestTemplate
                .exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<CollectionModel<EntityModel<CompanyDTO>>>() {
                        });

        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isNotNull().isEqualTo(HttpStatus.OK);
        Assertions.assertThat(exchange.getBody()).isNotNull().isNotEmpty();

        Assertions.assertThat(exchange.getBody().getContent().stream().filter(
                companyDTOEntityModel -> companyDTOEntityModel.getContent().getName().equals(nameExpected)
        ));

    }

    @Test
    @DisplayName("getByName returns an empty collection model of companyDTO when company is not found")
    void getByName_ReturnEmptyCollectionModelOfCompanyDto_WhenCompanyIsNotFound() {
        String nameExpected = "";
        String url = String.format("/companies/find?name=%s", nameExpected);
        ResponseEntity<CollectionModel<EntityModel<CompanyDTO>>> exchange = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<
                CollectionModel<EntityModel<CompanyDTO>>>() {
        });

        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(exchange.getBody()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("saveCompany returns an entity model of companyDTO when successful")
    void saveCompany_ReturnEntityModelOfCompanyDTO_WhenSuccessful() {
        String url = "/companies";
        CompanyDTO companyToBeSaved = CompanyDTOCreator.createCompanyDTO();
        ResponseEntity<EntityModel<CompanyDTO>> exchange = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(companyToBeSaved),
                new ParameterizedTypeReference<EntityModel<CompanyDTO>>() {
                });
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getBody()).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();

    }

    @Test
    @DisplayName("UpdateCompanyById returns a altered companyDTO")
    void updateCompanyById_ReturnEntityModelOfCompanyDto_WhenSuccessful() {
        String url = "/companies/{id}";
        CompanyDTO companyToUpdated = CompanyDTOCreator.createCompanyDTO();
        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        Long idCompanyExpected = companySaved.getId();
        Long idUpdated = companySaved.getId();
        ResponseEntity<EntityModel<CompanyDTO>> exchange = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(companyToUpdated),
                new ParameterizedTypeReference<EntityModel<CompanyDTO>>() {
                }, idUpdated);
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(exchange.getBody()).isNotNull();
        Assertions.assertThat(exchange.getBody().getContent().getName()).isEqualTo(companyToUpdated.getName());
        Assertions.assertThat(exchange.getBody().getContent().getDateOfFoundation()).isEqualTo(companyToUpdated.getDateOfFoundation());
        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty()
                .toString().contains("/companies/" + idUpdated);
    }

    @Test
    void getGamesByCompanyId_ReturnCollectionEntityModelOfGamesResponse_WhenSuccessful() {
        String url = "/companies/{id}/games";
        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        Long idCompany = companySaved.getId();
        ResponseEntity<CollectionModel<EntityModel<GameResponseDTO>>> exchange = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<CollectionModel<EntityModel<GameResponseDTO>>>() {
                }, idCompany);
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(exchange.getBody()).isNotNull();
        Assertions.assertThat(exchange.getBody().getContent()).isNotNull();
        Assertions.assertThat(exchange.getBody().getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("deleteCompanyById removes companyDTO when successful")
    void deleteCompanyById_DeleteCompany_WhenSuccessful() {
        Company companySaved = companyRepository.save(CompanyCreator.createCompanyToBeSaved());
        String url = "/companies/{id}";
        Long idToDelete = companySaved.getId();
        ResponseEntity<Void> exchange = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, idToDelete);
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

//    @AfterEach
//    void setup(){
//        companyRepository.deleteAll();
//    }
}
