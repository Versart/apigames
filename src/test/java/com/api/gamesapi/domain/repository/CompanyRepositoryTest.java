package com.api.gamesapi.domain.repository;

import com.api.gamesapi.api.model.CompanyDTO;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
@DisplayName("Tests for Company Repository")
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;



    public CompanyDTO createCompany() {
        return new CompanyDTO().builder().dateOfFoundation(LocalDate.now()).name("Test company").build();
    }

    //public void save_Throws
}