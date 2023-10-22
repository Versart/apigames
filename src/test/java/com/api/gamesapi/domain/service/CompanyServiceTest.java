package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
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

        BDDMockito.when(companyRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(CompanyCreator.createValidCompany()));

        BDDMockito.when(companyRepositoryMock.save(ArgumentMatchers.any(Company.class)))
                .thenReturn(CompanyCreator.createValidCompany());
        BDDMockito.when(companyRepositoryMock.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(true);
        BDDMockito.doNothing().when(companyRepositoryMock).deleteById(ArgumentMatchers.anyLong());

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
    @DisplayName("saveCompany returns company when Successful")
    void saveCompany_ReturnsCompany_WhenSuccessful() {
        EntityModel<CompanyResponse> companyDTOEntityModel = companyService.saveCompany(CompanyDTOCreator.createCompanyRequest());
        Assertions.assertThat(companyDTOEntityModel).isNotNull();
        Assertions.assertThat(companyDTOEntityModel.getLinks()).isNotNull().isNotEmpty();
        Assertions.assertThat(companyDTOEntityModel.getContent().getId()).isNotNull();
    }

    @Test
    @DisplayName("DeleteCompanyById remove company when Successful")
    void deleteCompanyById_RemovesCompany_WhenSuccessful() {
        Assertions.assertThatCode(() -> companyService.deleteCompanyById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("SearchCompanyById returns company when successful")
    void searchCompanyById_ReturnsCompany_WhenSuccessful() {
        EntityModel<CompanyResponse> companyDTOEntityModel = companyService.searchCompanyById(1L);

        Assertions.assertThat(companyDTOEntityModel).isNotNull();
        
        Assertions.assertThat(companyDTOEntityModel).isEqualTo(CompanyDTOCreator.createEntityModelCompanyResponse());
    }

    @Test
    @DisplayName("findCompanyByName returns list of companies when successful")
    void findCompanyByName_ReturnsListOfCompanies_WhenSuccessful() {
        PagedModel<EntityModel<CompanyResponse>> companiesByName = companyService.findCompanyByName("",PageRequest.of(0, 1));
        
        Assertions.assertThat(companiesByName).isNotNull().isNotEmpty().hasSize(1);
        
        Assertions.assertThat(companiesByName).contains(CompanyDTOCreator.createEntityModelCompanyResponse());
    }

    @Test
    @DisplayName("updateCompanyById returns company when successful")
    void updateCompanyById_ReturnsCompany_WhenSuccessful() {
        Long idExpected = 1l;
        CompanyRequest companyToBeUpdated = CompanyDTOCreator.createCompanyRequest();
        EntityModel<CompanyResponse> alteredCompany = companyService.updateCompanyById(idExpected, companyToBeUpdated);
        Assertions.assertThat(alteredCompany).isNotNull();
        Assertions.assertThat(alteredCompany.getLinks()).isNotNull().isNotEmpty();
        Assertions.assertThat(alteredCompany.getContent().getId()).isEqualTo(idExpected);
    }

    @Test
    @DisplayName("companyExists returns true when successful")
    void companyExists_ReturnsTrue_WhenSuccessful() {
        boolean companyExists = companyService.companyExists(1L);
        Assertions.assertThat(companyExists).isTrue();

    }

    @Test
    @DisplayName("listCompanies returns list of companies when successful")
    void listCompanies() {
        PagedModel<EntityModel<CompanyResponse>> pagedModel = companyService.listCompanies(PageRequest.of(1, 1));
        Assertions.assertThat(pagedModel).isNotNull().isNotEmpty();
        Assertions.assertThat(pagedModel.getLinks()).isNotNull().isNotEmpty();
        Assertions.assertThat(pagedModel.getContent().stream().toList()).contains(
                CompanyDTOCreator.createEntityModelCompanyResponse()
        );
        
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