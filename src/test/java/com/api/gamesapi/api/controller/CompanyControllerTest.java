package com.api.gamesapi.api.controller;


import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.CompanyService;
import com.api.gamesapi.util.CompanyDTOCreator;
import com.api.gamesapi.util.GameDTOCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController;
    @Mock
    private CompanyService companyServiceMock;



    @BeforeEach
    void setup() {
        List<EntityModel<CompanyResponse>> entityModels = new ArrayList<>();
        entityModels.add(CompanyDTOCreator.createEntityModelCompanyResponse());
        CollectionModel<EntityModel<CompanyResponse>> collectionModel = CollectionModel.of(entityModels);
        EntityModel<CompanyResponse> entityModelCompanyDto = CompanyDTOCreator.createEntityModelCompanyResponse();

        PagedModel<EntityModel<CompanyResponse>> companyPage = CompanyDTOCreator.createPageDModelCompanyDTO();

        BDDMockito.when(companyServiceMock.listCompanies(ArgumentMatchers.any())).thenReturn(companyPage);

        BDDMockito.when(companyServiceMock.searchCompanyById(ArgumentMatchers.anyLong())).thenReturn(
                entityModelCompanyDto
        );

        BDDMockito.when(companyServiceMock.findCompanyByName(ArgumentMatchers.anyString()))
                .thenReturn(collectionModel);

        BDDMockito.when(companyServiceMock.saveCompany(ArgumentMatchers.any(CompanyRequest.class)))
                .thenReturn(entityModelCompanyDto);

        BDDMockito.when(companyServiceMock.updateCompanyById(ArgumentMatchers.anyLong()
                ,ArgumentMatchers.any(CompanyRequest.class))).thenReturn(entityModelCompanyDto);

        BDDMockito.doNothing().when(companyServiceMock).deleteCompanyById(ArgumentMatchers.anyLong());
        BDDMockito.when(companyServiceMock.getGamesOfCompany(ArgumentMatchers.anyLong()))
                .thenReturn(GameDTOCreator.createPagedModelGameResponse());
    }

    @Test
    @DisplayName("listCompanies returns PagedModel of CompanyResponse when successful")
    void listCompanies_ReturnsPagedModelofCompanyDto_WhenSuccessful() {
        PagedModel<EntityModel<CompanyResponse>> companyPage = companyController.listCompanies(null).getBody();
        String nameCompany = CompanyDTOCreator.createCompanyRequest().getName();

        Assertions.assertThat(companyPage).isNotNull();

        Assertions.assertThat(companyPage).isNotEmpty().hasSize(1);

        Assertions.assertThat(companyPage.getContent().stream().toList().get(0).getContent().getName())
                .isEqualTo(nameCompany);

    }

    @Test
    @DisplayName("getCompanyById returns EntityModel of CompanyResponse when successful")
    void getCompanyById_RetursnEntityModelOfCompanyDto_WhenSuccessfull() {
        EntityModel<CompanyResponse> companyDTOModel = companyController.getCompanyById(1).getBody();

        EntityModel<CompanyResponse> companyForTest = CompanyDTOCreator.createEntityModelCompanyResponse();

        Assertions.assertThat(companyDTOModel).isNotNull();

        Assertions.assertThat(companyDTOModel.getContent()).isNotNull();

        Assertions.assertThat(companyDTOModel.getContent().getName())
                .isEqualTo(companyForTest.getContent().getName());
    }

    @Test
    @DisplayName("GetByName returns a CollectionModel of CompanyResponse when successful")
    void getByName_ReturnsCollectionModelOfCompanyDto_WhenSuccessful() {
       CollectionModel<EntityModel<CompanyResponse>> companies = companyController.getByName("company").getBody();

       EntityModel<CompanyRequest> company = EntityModel.of(CompanyDTOCreator.createCompanyRequest());

       Assertions.assertThat(companies).isNotNull().isNotEmpty().hasSize(1);

       Assertions.assertThat(companies.getContent().stream().toList().get(0).getContent().getName())
               .isEqualTo(company.getContent().getName());
    }

    @Test
    @DisplayName("getByName returns an empty CollectionModel of companyResponse when company is not found")
    void getByName_ReturnsEmptyCollectionModelOfCompanyDto_WhenCompanyIsNotFound() {
        BDDMockito.when(companyServiceMock.findCompanyByName(ArgumentMatchers.anyString()))
                .thenReturn(CollectionModel.empty());
        CollectionModel<EntityModel<CompanyResponse>> companies = companyController.getByName("company").getBody();

        Assertions.assertThat(companies).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("saveCompany returns an EntityModel of CompanyResponse when successful")
    void saveCompany_ReturnsEntityModelOfCompanyDTO_WhenSuccessful() {
        EntityModel<CompanyResponse> companyDTOExpected = CompanyDTOCreator.createEntityModelCompanyResponse();
        EntityModel<CompanyResponse> companyDto = companyController.saveCompany(CompanyDTOCreator.createCompanyRequest()).getBody();

        Assertions.assertThat(companyDto)
                .isNotNull()
                .isEqualTo(companyDTOExpected);
    }

    @Test
    @DisplayName("UpdateCompanyById returns a EntityModel of companyResponse when successful")
    void updateCompanyById_ReturnsEntityModelOfCompanyDto_WhenSuccessful() {
        Long companyExpected = 1l;
        EntityModel<CompanyResponse> companyDTOUpdated = companyController
                .updateCompanyById(companyExpected,CompanyDTOCreator.createCompanyRequest()).getBody();

        Assertions.assertThat(companyDTOUpdated).isNotNull();
        Assertions.assertThat(companyDTOUpdated.getLinks()).isNotNull().isNotEmpty();
        Assertions.assertThat(companyDTOUpdated.getContent().getId())
                .isEqualTo(companyExpected);

    }
    @Test
    @DisplayName("getGamesByCompanyId returns CollectionEntityModel of GameResponse when successful")
    void getGamesByCompanyId_ReturnsCollectionEntityModelOfGamesResponse_WhenSuccessful() {
        CollectionModel<EntityModel<GameResponseDTO>> gamesByCompanyId =
                companyController.getGamesByCompanyId(1l).getBody();
        Assertions.assertThat(gamesByCompanyId).isNotNull().isNotEmpty();
        Assertions.assertThat(gamesByCompanyId).contains(GameDTOCreator.createEntityModelGameResponse());
    }

    @Test
    @DisplayName("deleteCompanyById removes company when successful")
    void deleteCompanyById_DeletesCompany_WhenSuccessful() {
        Assertions.assertThatCode(() -> companyController.deleteCompanyById(1l) )
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = companyController.deleteCompanyById(1l);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}