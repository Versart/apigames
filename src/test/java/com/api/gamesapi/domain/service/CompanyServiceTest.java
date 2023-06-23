package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.util.CompanyCreator;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        PagedModel<EntityModel<CompanyDTO>> pageDModelCompanyDTO = CompanyDTOCreator.createPageDModelCompanyDTO();

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
                .thenReturn(CompanyDTOCreator.createEntityModelCompanyDTO());
        BDDMockito.when(companyMapperMock.toEntity(ArgumentMatchers.any(CompanyDTO.class)))
                .thenReturn(CompanyCreator.createValidCompany());
        BDDMockito.when(companyMapperMock.toModelPage(ArgumentMatchers.any(Page.class)))
                .thenReturn(pageDModelCompanyDTO);
        BDDMockito.when(companyMapperMock.toModelList(ArgumentMatchers.any(List.class))).thenReturn(
                CompanyDTOCreator.createCollectionModelCompanyDTO()
        );
        BDDMockito.doNothing().when(companyRepositoryMock).deleteById(ArgumentMatchers.anyLong());
        BDDMockito.when(gameServiceMock.listGamesByCompanyId(ArgumentMatchers.anyLong()))
                .thenReturn(GameResponseCreator.createCollectionModelGameResponse());
    }

    @Test
    @DisplayName("saveCompany returns company when Successful")
    void saveCompany_ReturnsCompany_WhenSuccessful() {
        EntityModel<CompanyDTO> companyDTOEntityModel = companyService.saveCompany(CompanyDTOCreator.createCompanyDTO());
        Assertions.assertThat(companyDTOEntityModel).isNotNull();
        Assertions.assertThat(companyDTOEntityModel.getContent()).isEqualTo(CompanyDTOCreator.createCompanyDTO());
    }

    @Test
    @DisplayName("DeleteCompanyById remove company when Successful")
    void deleteCompanyById_RemoveCompany_WhenSuccessful() {
        Assertions.assertThatCode(() -> companyService.deleteCompanyById(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("SearchCompanyById returns company when successful")
    void searchCompanyById_ReturnsCompany_WhenSuccessful() {
        EntityModel<CompanyDTO> companyDTOEntityModel = companyService.searchCompanyById(1L);

        Assertions.assertThat(companyDTOEntityModel).isNotNull();
        Assertions.assertThat(companyDTOEntityModel).isEqualTo(CompanyDTOCreator.createEntityModelCompanyDTO());
    }

    @Test
    @DisplayName("findCompanyByName returns list of companies when successful")
    void findCompanyByName_ReturnsListOfCompanies_WhenSuccessful() {
        CollectionModel<EntityModel<CompanyDTO>> companiesByName = companyService.findCompanyByName("");
        Assertions.assertThat(companiesByName).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(companiesByName).contains(CompanyDTOCreator.createEntityModelCompanyDTO());
    }

    @Test
    @DisplayName("updateCompanyById returns company when successful")
    void updateCompanyById_ReturnsCompany_WhenSuccessful() {
        CompanyDTO companyToBeUpdated = CompanyDTOCreator.createCompanyDTO();
        EntityModel<CompanyDTO> alteredCompany = companyService.updateCompanyById(1L, companyToBeUpdated);
        Assertions.assertThat(alteredCompany).isNotNull().isEqualTo(EntityModel.of(companyToBeUpdated));
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
        PagedModel<EntityModel<CompanyDTO>> pagedModel = companyService.listCompanies(PageRequest.of(1, 1));
        Assertions.assertThat(pagedModel).isNotNull().isNotEmpty();
        Assertions.assertThat(pagedModel).contains(CompanyDTOCreator.createEntityModelCompanyDTO());
    }

    @Test
    @DisplayName("getGamesOfCompany returns list of company games when successful")
    void getGamesOfCompany_ReturnsListOfComapnyGames_WhenSuccessful() {
        CollectionModel<EntityModel<GameResponseDTO>> gamesOfCompany = companyService.getGamesOfCompany(1l);
        Assertions.assertThat(gamesOfCompany).isNotNull().isNotEmpty();
        Assertions.assertThat(gamesOfCompany).contains(GameResponseCreator.createEntityModelGameResponse());
    }
    @Test
    @DisplayName("getGamesOfCompany throws companyNotFoundException when company is not found")
    void getGamesOfCompany_ThrowsCompanyNotFoundException_WhenCompanyIsNotFound() {
        BDDMockito.when(gameServiceMock.listGamesByCompanyId(ArgumentMatchers.anyLong()))
                .thenThrow(CompanyNotFoundException.class);
        Assertions.assertThatExceptionOfType(CompanyNotFoundException.class)
                .isThrownBy(() -> companyService.getGamesOfCompany(1l));
    }
}