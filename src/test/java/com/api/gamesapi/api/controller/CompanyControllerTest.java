package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.NotFoundException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CompanyService companyServiceMock;

    @BeforeEach
    void setup() {
       
        EntityModel<CompanyResponse> entityModelCompanyDto = CompanyDTOCreator.createEntityModelCompanyResponse();

        PagedModel<EntityModel<CompanyResponse>> companyPage = CompanyDTOCreator.createPageDModelCompanyDTO();

        BDDMockito.when(companyServiceMock.listCompanies(ArgumentMatchers.any(PageRequest.class))).thenReturn(companyPage);

        BDDMockito.when(companyServiceMock.searchCompanyById(ArgumentMatchers.anyLong())).thenReturn(
                entityModelCompanyDto
        );

        BDDMockito.when(companyServiceMock.findCompanyByName(ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(companyPage);

        BDDMockito.when(companyServiceMock.saveCompany(ArgumentMatchers.any(CompanyRequest.class)))
                .thenReturn(entityModelCompanyDto);

        BDDMockito.when(companyServiceMock.updateCompanyById(ArgumentMatchers.anyLong()
                ,ArgumentMatchers.any(CompanyRequest.class))).thenReturn(entityModelCompanyDto);

        BDDMockito.doNothing().when(companyServiceMock).deleteCompanyById(ArgumentMatchers.anyLong());
        
        BDDMockito.when(companyServiceMock.getGamesOfCompany(ArgumentMatchers.anyLong(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(GameDTOCreator.createPagedModelGameResponse());
    }

    @Test
    @DisplayName("listCompanies returns PagedModel of CompanyResponse when successful")
    void listCompanies_ReturnsPagedModelofCompanyResponse_WhenSuccessful() {
        String nameCompany = CompanyDTOCreator.createCompanyRequest().getName();

        PagedModel<EntityModel<CompanyResponse>> companyPage = companyController.listCompanies(PageRequest.of(0, 1)).getBody();
        
        Assertions.assertThat(companyPage).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(companyPage.getContent().stream().toList().get(0).getContent().getName())
                .isEqualTo(nameCompany);
        
        Assertions.assertThat(companyPage.getContent().stream().toList().get(0).getLinks())
                .isNotNull().isNotEmpty();
        
        Assertions.assertThat(companyPage.getLinks()).isNotNull().isNotEmpty();

    }

    @Test
    @DisplayName("listCompanies returns empty PagedModel of CompanyResponse when no company is found")
    void listCompanies_ReturnsEmptyPagedModelofCompanyResponse_WhenNoCompanyIsFound() {
        BDDMockito.when(companyServiceMock.listCompanies(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(PagedModel.empty());
        
        PagedModel<EntityModel<CompanyResponse>> companyPage = companyController.listCompanies(PageRequest.of(0, 1)).getBody();
        
        Assertions.assertThat(companyPage).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("getCompanyById returns EntityModel of CompanyResponse when successful")
    void getCompanyById_RetursnEntityModelOfCompanyDto_WhenSuccessfull() {
        Long idExpected = CompanyDTOCreator.createEntityModelCompanyResponse().getContent().getId();
        
        EntityModel<CompanyResponse> company = companyController.getCompanyById(1).getBody();

        Assertions.assertThat(company).isNotNull();

        Assertions.assertThat(company.getContent()).isNotNull();

        Assertions.assertThat(company.getContent().getId())
                .isEqualTo(idExpected);
        
        Assertions.assertThat(company.getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("getCompanyById throws NotFoundException when Company is not found")
    void getCompanyById_ThrowsNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.when(companyServiceMock.searchCompanyById(ArgumentMatchers.anyLong()))
                .thenThrow(NotFoundException.class);

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> companyController.getCompanyById(1l));
    }


    @Test
    @DisplayName("GetByName returns a PagedModel of CompanyResponse when successful")
    void getByName_ReturnsPagedModelOfCompanyResponse_WhenSuccessful() {
       EntityModel<CompanyRequest> company = EntityModel.of(CompanyDTOCreator.createCompanyRequest());
       
       PagedModel<EntityModel<CompanyResponse>> page = companyController.getByName("company", PageRequest.of(0, 1)).getBody();
       
       Assertions.assertThat(page).isNotNull().isNotEmpty().hasSize(1);

       Assertions.assertThat(page.getContent().stream().toList().get(0).getContent().getName())
               .contains(company.getContent().getName());

       Assertions.assertThat(page.getContent().stream().toList().get(0).getLinks())
                .isNotNull().isNotEmpty();

        Assertions.assertThat(page.getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("getByName returns an empty PagedModel of companyResponse when company is not found")
    void getByName_ReturnsEmptyCollectionModelOfCompanyDto_WhenCompanyIsNotFound() {
        BDDMockito.when(companyServiceMock.findCompanyByName(ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(PagedModel.empty());
        
        PagedModel<EntityModel<CompanyResponse>> companies = companyController.getByName("company",PageRequest.of(0, 1)).getBody();

        Assertions.assertThat(companies).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("saveCompany returns an EntityModel of CompanyResponse when successful")
    void saveCompany_ReturnsEntityModelOfCompanyDTO_WhenSuccessful() {
        CompanyRequest companyToBeSaved = CompanyDTOCreator.createCompanyRequest();
        
        EntityModel<CompanyResponse> companySaved = companyController.saveCompany(companyToBeSaved).getBody();

        Assertions.assertThat(companySaved).isNotNull();

        Assertions.assertThat(companySaved.getContent().getId()).isNotNull();

        Assertions.assertThat(companySaved.getLinks()).isNotNull().isNotEmpty();
                
    }

    @Test
    @DisplayName("UpdateCompanyById returns a EntityModel of CompanyResponse when successful")
    void updateCompanyById_ReturnsEntityModelOfCompanyDto_WhenSuccessful() {
        Long companyExpected = CompanyDTOCreator.createCompanyResponse().getId();

        EntityModel<CompanyResponse> companyDTOUpdated = companyController
                .updateCompanyById(companyExpected,CompanyDTOCreator.createCompanyRequest()).getBody();

        Assertions.assertThat(companyDTOUpdated).isNotNull();

        Assertions.assertThat(companyDTOUpdated.getContent().getId())
                .isEqualTo(companyExpected);

        Assertions.assertThat(companyDTOUpdated.getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("UpdateCompanyById throws NotFoundException when Company is not found")
    void updateCompanyById_ThrowsNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.when(companyServiceMock
                .updateCompanyById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(CompanyRequest.class)))
                .thenThrow(NotFoundException.class);
        
        CompanyRequest companyToBeUpdated = CompanyDTOCreator.createCompanyRequest();
       
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> companyController.updateCompanyById(1l, companyToBeUpdated));
    }


    @Test
    @DisplayName("getGamesByCompanyId returns PagedModel of GameResponse when successful")
    void getGamesByCompanyId_ReturnsPagedModelOfGamesResponse_WhenSuccessful() {
        String companyName = CompanyDTOCreator.createCompanyResponse().getName();

        PagedModel<EntityModel<GameResponseDTO>> gamesByCompany =
                companyController.getGamesByCompanyId(1l,PageRequest.of(0, 1)).getBody();
        
        Assertions.assertThat(gamesByCompany).isNotNull().isNotEmpty();
        
        Assertions.assertThat(gamesByCompany.getContent().stream().toList().get(0)
                .getContent().getCompanyName()).isEqualTo(companyName);
        
        Assertions.assertThat(gamesByCompany.getContent().stream().toList().get(0)
                .getLinks()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(gamesByCompany.getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("getGamesByCompanyId returns empty PagedModel of GameResponse when no game is found")
    void getGamesByCompanyId_ReturnsEmptyPagedModelOfGamesResponse_WhenNogameIsFound() {
        BDDMockito.when(companyServiceMock.getGamesOfCompany(ArgumentMatchers.anyLong(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(PagedModel.empty());
        
        PagedModel<EntityModel<GameResponseDTO>> gamesByCompany =
                companyController.getGamesByCompanyId(1l,PageRequest.of(0, 1)).getBody();
        
        Assertions.assertThat(gamesByCompany).isNotNull().isEmpty();
        
    }

    @Test
    @DisplayName("deleteCompanyById removes Company when successful")
    void deleteCompanyById_DeletesCompany_WhenSuccessful() {
        Assertions.assertThatCode(() -> companyController.deleteCompanyById(1l) )
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = companyController.deleteCompanyById(1l);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteCompanyById throws NotFoundException when Company is not found")
    void deleteCompanyById_ThrowsNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.doThrow(NotFoundException.class)
                .when(companyServiceMock).deleteCompanyById(ArgumentMatchers.anyLong());
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> companyController.deleteCompanyById(1l));
    }
}