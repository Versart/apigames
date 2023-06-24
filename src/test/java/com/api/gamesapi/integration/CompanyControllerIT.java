package com.api.gamesapi.integration;

import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.util.CompanyCreator;
import com.api.gamesapi.util.CompanyDTOCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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
        CompanyDTOCreator.createPageDModelCompanyDTO();
        Assertions.assertThat(exchange).isNotNull();
        Assertions.assertThat(exchange.getBody()).isNotNull().isNotEmpty();
        Assertions.assertThat(exchange.getBody().getContent().contains(companySaved));
    }

    @AfterEach
    void setup(){
        companyRepository.deleteAll();
    }
}
