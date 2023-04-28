package com.api.gamesapi.api.controller;


import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.service.CompanyService;
import com.api.gamesapi.util.CompanyDTOCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    CompanyService companyServiceMock;

    @BeforeEach
    void setup() {
        List<EntityModel<CompanyDTO>> entityModels = new ArrayList<>();
        entityModels.add(EntityModel.of(CompanyDTOCreator.createCompany()));
        PagedModel<EntityModel<CompanyDTO>> companyPage;
        companyPage = PagedModel.of(entityModels,new PagedModel.PageMetadata(1,1,1));
        CollectionModel<EntityModel<CompanyDTO>> collectionModel = CollectionModel.of(entityModels);
        EntityModel<CompanyDTO> entityModelCompanyDto = EntityModel.of(CompanyDTOCreator.createCompany());

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
    }

    @Test
    void listCompanies_ReturnPageofCompanyDto_WhenSuccessful() {
        PagedModel<EntityModel<CompanyDTO>> companyPage = companyController.listCompanies(null).getBody();
        String nameCompany = CompanyDTOCreator.createCompany().getName();

        Assertions.assertThat(companyPage).isNotNull();

        Assertions.assertThat(companyPage).isNotEmpty();

        Assertions.assertThat(companyPage.getContent().stream().toList().get(0).getContent().getName())
                .isEqualTo(nameCompany);
    }

    @Test
    void getCompanyById_ReturnEntityModelOfCompanyDto_WhenSuccessfull() {
        EntityModel<CompanyDTO> companyDTOModel = companyController.getCompanyById(1l).getBody();

        EntityModel<CompanyDTO> companyForTest = EntityModel.of(CompanyDTOCreator.createCompany());

        Assertions.assertThat(companyDTOModel).isNotNull();

        Assertions.assertThat(companyDTOModel.getContent()).isNotNull();

        Assertions.assertThat(companyDTOModel.getContent().getName())
                .isEqualTo(companyForTest.getContent().getName());
    }

    @Test
    void getByName_ReturnCollectionModelOfCompanyDto_WhenSuccessful() {
       CollectionModel<EntityModel<CompanyDTO>> companies = companyController.getByName("company").getBody();

       EntityModel<CompanyDTO> company = EntityModel.of(CompanyDTOCreator.createCompany());

       Assertions.assertThat(companies).isNotNull().isNotEmpty().hasSize(1);

       Assertions.assertThat(companies.getContent().stream().toList().get(0).getContent().getName())
               .isEqualTo(company.getContent().getName());
    }

    @Test
    void getByName_ReturnEmptyCollectionModelOfCompanyDto_WhenCompanyIsNotFound() {
        BDDMockito.when(companyServiceMock.findCompanyByName(ArgumentMatchers.anyString()))
                .thenReturn(CollectionModel.empty());
        CollectionModel<EntityModel<CompanyDTO>> companies = companyController.getByName("company").getBody();

        Assertions.assertThat(companies).isNotNull().isEmpty();
    }

    @Test
    void saveCompany_ReturnEntityModelOfCompanyDTO_WhenSuccessful() {
        EntityModel<CompanyDTO> companyDto = companyController.saveCompany(CompanyDTOCreator.createCompany());

        Assertions.assertThat(companyDto).isNotNull()
                .isEqualTo(EntityModel.of(CompanyDTOCreator.createCompany()));
    }

    @Test
    void updateCompanyById_ReturnEntityModelOfCompanyDto_WhenSuccessful() {
        EntityModel<CompanyDTO> companyDTOUpdated = companyController
                .updateCompanyById(1l,CompanyDTOCreator.createCompany()).getBody();

        Assertions.assertThat(companyDTOUpdated).isNotNull()
                .isEqualTo(EntityModel.of(CompanyDTOCreator.createCompany()));

    }

    @Test
    void deleteCompanyById_DeleteCompany_WhenSuccessful() {
        Assertions.assertThatCode(() -> companyController.deleteCompanyById(1l) )
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = companyController.deleteCompanyById(1l);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}