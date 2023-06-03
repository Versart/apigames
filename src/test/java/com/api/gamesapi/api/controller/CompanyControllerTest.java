package com.api.gamesapi.api.controller;


import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.CompanyService;
import com.api.gamesapi.util.CompanyDTOCreator;
import com.api.gamesapi.util.GameResponseCreator;
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
        List<EntityModel<CompanyDTO>> entityModels = new ArrayList<>();
        entityModels.add(CompanyDTOCreator.createEntityModelCompanyDTO());
        CollectionModel<EntityModel<CompanyDTO>> collectionModel = CollectionModel.of(entityModels);
        EntityModel<CompanyDTO> entityModelCompanyDto = CompanyDTOCreator.createEntityModelCompanyDTO();

        PagedModel<EntityModel<CompanyDTO>> companyPage = CompanyDTOCreator.createPageDModelCompanyDTO();

        BDDMockito.when(companyServiceMock.listCompanies(ArgumentMatchers.any())).thenReturn(companyPage);

        BDDMockito.when(companyServiceMock.searchCompanyById(ArgumentMatchers.anyLong())).thenReturn(
                entityModelCompanyDto
        );

        BDDMockito.when(companyServiceMock.findCompanyByName(ArgumentMatchers.anyString()))
                .thenReturn(collectionModel);

        BDDMockito.when(companyServiceMock.saveCompany(ArgumentMatchers.any(CompanyDTO.class)))
                .thenReturn(entityModelCompanyDto);

        BDDMockito.when(companyServiceMock.updateCompanyById(ArgumentMatchers.anyLong()
                ,ArgumentMatchers.any(CompanyDTO.class))).thenReturn(entityModelCompanyDto);

        BDDMockito.doNothing().when(companyServiceMock).deleteCompanyById(ArgumentMatchers.anyLong());
        BDDMockito.when(companyServiceMock.getGamesOfCompany(ArgumentMatchers.anyLong()))
                .thenReturn(GameResponseCreator.createCollectionModelGameResponse());
    }

    @Test
    @DisplayName("List returns PagedModel of CompanyDTO when successful")
    void listCompanies_ReturnPageofCompanyDto_WhenSuccessful() {
        PagedModel<EntityModel<CompanyDTO>> companyPage = companyController.listCompanies(null).getBody();
        String nameCompany = CompanyDTOCreator.createCompanyDTO().getName();

        Assertions.assertThat(companyPage).isNotNull();

        Assertions.assertThat(companyPage).isNotEmpty().hasSize(1);

        Assertions.assertThat(companyPage.getContent().stream().toList().get(0).getContent().getName())
                .isEqualTo(nameCompany);

    }

    @Test
    @DisplayName("getCompanyById returns model of companyDTO when successful")
    void getCompanyById_ReturnEntityModelOfCompanyDto_WhenSuccessfull() {
        EntityModel<CompanyDTO> companyDTOModel = companyController.getCompanyById(1).getBody();

        EntityModel<CompanyDTO> companyForTest = CompanyDTOCreator.createEntityModelCompanyDTO();

        Assertions.assertThat(companyDTOModel).isNotNull();

        Assertions.assertThat(companyDTOModel.getContent()).isNotNull();

        Assertions.assertThat(companyDTOModel.getContent().getName())
                .isEqualTo(companyForTest.getContent().getName());
    }

    @Test
    @DisplayName("GetByName returns a collection model of companyDTO when successful")
    void getByName_ReturnCollectionModelOfCompanyDto_WhenSuccessful() {
       CollectionModel<EntityModel<CompanyDTO>> companies = companyController.getByName("company").getBody();

       EntityModel<CompanyDTO> company = EntityModel.of(CompanyDTOCreator.createCompanyDTO());

       Assertions.assertThat(companies).isNotNull().isNotEmpty().hasSize(1);

       Assertions.assertThat(companies.getContent().stream().toList().get(0).getContent().getName())
               .isEqualTo(company.getContent().getName());
    }

    @Test
    @DisplayName("getByName returns an empty collection model of companyDTO when company is not found")
    void getByName_ReturnEmptyCollectionModelOfCompanyDto_WhenCompanyIsNotFound() {
        BDDMockito.when(companyServiceMock.findCompanyByName(ArgumentMatchers.anyString()))
                .thenReturn(CollectionModel.empty());
        CollectionModel<EntityModel<CompanyDTO>> companies = companyController.getByName("company").getBody();

        Assertions.assertThat(companies).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("saveCompany returns an entity model of companyDTO when successful")
    void saveCompany_ReturnEntityModelOfCompanyDTO_WhenSuccessful() {
        EntityModel<CompanyDTO> companyDTOExpected = CompanyDTOCreator.createEntityModelCompanyDTO();
        EntityModel<CompanyDTO> companyDto = companyController.saveCompany(CompanyDTOCreator.createCompanyDTO());

        Assertions.assertThat(companyDto)
                .isNotNull()
                .isEqualTo(companyDTOExpected);
    }

    @Test
    @DisplayName("UpdateCompanyById returns a altered companyDTO")
    void updateCompanyById_ReturnEntityModelOfCompanyDto_WhenSuccessful() {
        EntityModel<CompanyDTO> companyDTOUpdated = companyController
                .updateCompanyById(1l,CompanyDTOCreator.createCompanyDTO()).getBody();

        Assertions.assertThat(companyDTOUpdated).isNotNull()
                .isEqualTo(EntityModel.of(CompanyDTOCreator.createCompanyDTO()));

    }
    @Test
    void getGamesByCompanyId_ReturnCollectionEntityModelOfGamesResponse_WhenSuccessful() {
        CollectionModel<EntityModel<GameResponseDTO>> gamesByCompanyId =
                companyController.getGamesByCompanyId(1l).getBody();
        Assertions.assertThat(gamesByCompanyId).isNotNull().isNotEmpty();
        Assertions.assertThat(gamesByCompanyId).contains(GameResponseCreator.createEntityModelGameResponse());
    }

    @Test
    @DisplayName("deleteCompanyById removes companyDTO when successful")
    void deleteCompanyById_DeleteCompany_WhenSuccessful() {
        Assertions.assertThatCode(() -> companyController.deleteCompanyById(1l) )
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = companyController.deleteCompanyById(1l);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}