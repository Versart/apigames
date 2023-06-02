package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.repository.CompanyRepository;
import com.api.gamesapi.util.CompanyCreator;
import com.api.gamesapi.util.CompanyDTOCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        Page<Company> companyPage =  new PageImpl<>(List.of(CompanyCreator.createValidCompany()));

        BDDMockito.when(companyRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                        .thenReturn(companyPage);


        BDDMockito.when(companyRepositoryMock.findById(ArgumentMatchers.anyLong()))
                        .thenReturn(Optional.of(CompanyCreator.createValidCompany()));

        BDDMockito.when(companyRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(CompanyCreator.createValidCompany()));

        BDDMockito.when(companyRepositoryMock.save(ArgumentMatchers.any(Company.class)))
                .thenReturn(CompanyCreator.createValidCompany());

        BDDMockito.when(companyMapperMock.toModel(ArgumentMatchers.any(Company.class)))
                        .thenReturn(CompanyDTOCreator.createEntityModelCompanyDTO());
        BDDMockito.when(companyMapperMock.toEntity(ArgumentMatchers.any(CompanyDTO.class)))
                        .thenReturn(CompanyCreator.createValidCompany());


        BDDMockito.doNothing().when(companyRepositoryMock).deleteById(ArgumentMatchers.anyLong());
    }
    @Test
    void saveCompany() {
        EntityModel<CompanyDTO> companyDTOEntityModel = companyService.saveCompany(CompanyDTOCreator.createCompanyDTO());
        Assertions.assertThat(companyDTOEntityModel).isNotNull();
        Assertions.assertThat(companyDTOEntityModel.getContent()).isEqualTo(CompanyDTOCreator.createCompanyDTO());

    }

    @Test
    void deleteCompanyById() {
    }

    @Test
    void searchCompanyById() {
    }

    @Test
    void findCompanyByName() {
    }

    @Test
    void updateCompanyById() {
    }

    @Test
    void companyExists() {
    }

    @Test
    void listCompanies() {
    }

    @Test
    void getGamesOfCompany() {
    }
}