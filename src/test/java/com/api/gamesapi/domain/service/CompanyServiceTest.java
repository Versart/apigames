package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.util.CompanyCreator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;

    @Mock
    CompanyRepository companyRepositoryMock;

    @Mock
    CompanyMapper companyMapperMock;

    @Mock
    GameService gameServiceMock;

    @BeforeEach
    void setup() {
        Page<Company> companyPage = new PageImpl<>(List.of(CompanyCreator.createValidCompany()));

        PagedModel<EntityModel<CompanyResponse>> pageDModelCompanyDTO = CompanyDTOCreator.createPageDModelCompanyDTO();

        BDDMockito.when(companyRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(companyPage);
        BDDMockito.when(companyRepositoryMock.existsById(ArgumentMatchers.anyLong())).thenReturn(true);

        BDDMockito.when(companyRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(CompanyCreator.createValidCompany()));

        BDDMockito.when(companyRepositoryMock.findByName(ArgumentMatchers.anyString(), 
                ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(companyPage);

        BDDMockito.when(companyRepositoryMock.save(ArgumentMatchers.any(Company.class)))
                .thenReturn(CompanyCreator.createValidCompany());
        BDDMockito.when(companyRepositoryMock.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(true);
        BDDMockito.doNothing().when(companyRepositoryMock).deleteById(ArgumentMatchers.anyLong());
        BDDMockito.when(companyRepositoryMock.findByNameContains(ArgumentMatchers.anyString(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(companyPage);
        BDDMockito.when(companyMapperMock.toModel(ArgumentMatchers.any(Company.class)))
                .thenReturn(CompanyDTOCreator.createEntityModelCompanyResponse());
        BDDMockito.when(companyMapperMock.toEntity(ArgumentMatchers.any(CompanyRequest.class)))
                .thenReturn(CompanyCreator.createValidCompany());
        BDDMockito.when(companyMapperMock.toModelPage(ArgumentMatchers.any(Page.class)))
                .thenReturn(pageDModelCompanyDTO);
        BDDMockito.when(companyMapperMock.toModelList(ArgumentMatchers.any(List.class))).thenReturn(
                CompanyDTOCreator.createCollectionModelCompanyDTO()
        );
        BDDMockito.doNothing().when(companyRepositoryMock).deleteById(ArgumentMatchers.anyLong());
        BDDMockito.when(gameServiceMock.listGamesByCompanyId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(GameDTOCreator.createPagedModelGameResponse());
    }

    @Test
    @DisplayName("saveCompany returns Company when Successful")
    void saveCompany_ReturnsCompany_WhenSuccessful() {
        EntityModel<CompanyResponse> companyDTOEntityModel = companyService.saveCompany(CompanyDTOCreator.createCompanyRequest());
        
        Assertions.assertThat(companyDTOEntityModel).isNotNull();
         
        Assertions.assertThat(companyDTOEntityModel.getContent().getId()).isNotNull();

         Assertions.assertThat(companyDTOEntityModel.getLinks()).isNotNull().isNotEmpty();
    }



    @Test
    @DisplayName("DeleteCompanyById remove company when Successful")
    void deleteCompanyById_RemovesCompany_WhenSuccessful() {
        Assertions.assertThatCode(() -> companyService.deleteCompanyById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("DeleteCompanyById throws NotFoundException when company is not found")
    void deleteCompanyById_ThrowsNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.when(companyRepositoryMock.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(false);

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> companyService.deleteCompanyById(1l))
                .withMessageContaining("Company not found with id");
    }

    @Test
    @DisplayName("SearchCompanyById returns company when successful")
    void searchCompanyById_ReturnsCompany_WhenSuccessful() {
        Long idExpected = CompanyCreator.createValidCompany().getId();

        EntityModel<CompanyResponse> company = companyService.searchCompanyById(idExpected);

        Assertions.assertThat(company).isNotNull();
        
        Assertions.assertThat(company.getContent().getId()).isEqualTo(idExpected);

        Assertions.assertThat(company.getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("SearchCompanyById throws NotFoundException when Company is not found")
    void searchCompanyById_ThrowsNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.when(companyRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> companyService.searchCompanyById(1l))
                .withMessageContaining("Company not found with id");
    }

    @Test
    @DisplayName("findCompanyByName returns list of Company when successful")
    void findCompanyByName_ReturnsListOfCompanies_WhenSuccessful() {
        String nameExpected = CompanyDTOCreator.createCompanyRequest().getName();
        
        PagedModel<EntityModel<CompanyResponse>> companiesByName = companyService.findCompanyByName(nameExpected,PageRequest.of(0, 1));
        
        Assertions.assertThat(companiesByName).isNotNull().isNotEmpty().hasSize(1);
        
        Assertions.assertThat(companiesByName.getContent().stream().toList().get(0)
                .getContent().getName()).contains(nameExpected);
        
        Assertions.assertThat(companiesByName.getContent().stream().toList().get(0)
                .getLinks()).isNotNull().isNotEmpty();
        
        Assertions.assertThat(companiesByName.getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("findCompanyByName returns empty list of Company when no Company is found")
    void findCompanyByName_ReturnsEmptyListOfCompany_WhenNoCompanyIsFound() {
        BDDMockito.when(companyRepositoryMock.findByNameContains(ArgumentMatchers.anyString(), 
                ArgumentMatchers.any(PageRequest.class))).thenReturn(Page.empty());

        BDDMockito.when(companyMapperMock.toModelPage(ArgumentMatchers.any(Page.class)))
                .thenReturn(PagedModel.empty());
        
        PagedModel<EntityModel<CompanyResponse>> companiesByName = companyService.findCompanyByName("",PageRequest.of(0, 1));
        
        Assertions.assertThat(companiesByName).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("updateCompanyById returns Company when successful")
    void updateCompanyById_ReturnsCompany_WhenSuccessful() {
        Long idExpected = CompanyCreator.createValidCompany().getId();
        
        CompanyRequest companyToBeUpdated = CompanyDTOCreator.createCompanyRequest();
        
        EntityModel<CompanyResponse> alteredCompany = companyService.updateCompanyById(idExpected, companyToBeUpdated);
        
        Assertions.assertThat(alteredCompany).isNotNull();
        
        Assertions.assertThat(alteredCompany.getContent().getId()).isEqualTo(idExpected);

        Assertions.assertThat(alteredCompany.getLinks()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("updateCompanyById throws NotFoundException when Company is not found")
    void updateCompanyById_ThrowsNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.when(companyRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Long idExpected = 1l;
        
        CompanyRequest companyToBeUpdated = CompanyDTOCreator.createCompanyRequest();
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> companyService.updateCompanyById(idExpected, companyToBeUpdated));
    }

    @Test
    @DisplayName("companyExists returns true when successful")
    void companyExists_ReturnsTrue_WhenSuccessful() {
        long idExpected = CompanyCreator.createValidCompany().getId();

        boolean companyExists = companyService.companyExists(idExpected);
        
        Assertions.assertThat(companyExists).isTrue();

    }

    @Test
    @DisplayName("companyExists returns true when successful")
    void companyExists_ThrowsNotFoundException_WhenCompanyNotExists() {
        BDDMockito.when(companyRepositoryMock.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(false);

        long idExpected = 1l;
        
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> companyService.companyExists(idExpected));

    }

    @Test
    @DisplayName("listCompanies returns PagedModel of CompanyResponse when successful")
    void listCompanies_ReturnsPagedModelOfCompanyResponse_WhenSuccessful() {
        String nameExpected = CompanyDTOCreator.createCompanyResponse().getName();

        PagedModel<EntityModel<CompanyResponse>> pagedModel = companyService.listCompanies(PageRequest.of(1, 1));
        
        Assertions.assertThat(pagedModel).isNotNull().isNotEmpty();
        
        Assertions.assertThat(pagedModel.getContent().stream().toList().get(0).getContent()
                .getName()).isEqualTo(nameExpected);
        
         Assertions.assertThat(pagedModel.getContent().stream().toList().get(0).getLinks())
                .isNotNull().isNotEmpty();
                
        Assertions.assertThat(pagedModel.getLinks()).isNotNull().isNotEmpty();
        
    }

    @Test
    @DisplayName("listCompanies returns PagedModel of CompanyResponse when successful")
    void listCompanies_ReturnsEmptyPagedModelOfCompanyResponse_WhenNoCompanyIsFound() {
        BDDMockito.when(companyRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(Page.empty());
        
        BDDMockito.when(companyMapperMock.toModelPage(ArgumentMatchers.any(Page.class)))
                .thenReturn(PagedModel.empty());
        
        PagedModel<EntityModel<CompanyResponse>> pagedModel = companyService.listCompanies(PageRequest.of(1, 1));
        
        Assertions.assertThat(pagedModel).isNotNull().isEmpty();
         
    }


    @Test
    @DisplayName("getGamesOfCompany returns list of company games when successful")
    void getGamesOfCompany_ReturnsListOfComapnyGames_WhenSuccessful() {
        CollectionModel<EntityModel<GameResponseDTO>> gamesOfCompany = companyService.getGamesOfCompany(1l, PageRequest.of(0, 1));
        
        Assertions.assertThat(gamesOfCompany).isNotNull().isNotEmpty();

        Assertions.assertThat(gamesOfCompany).contains(GameDTOCreator.createEntityModelGameResponse());
    }

    @Test
    @DisplayName("getGamesOfCompany throws companyNotFoundException when company is not found")
    void getGamesOfCompany_ThrowsCompanyNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.when(gameServiceMock.listGamesByCompanyId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(PageRequest.class)))
                .thenThrow(CompanyNotFoundException.class);
        
        Assertions.assertThatExceptionOfType(CompanyNotFoundException.class)
                .isThrownBy(() -> companyService.getGamesOfCompany(1l, PageRequest.of(0, 1)));
    }
}