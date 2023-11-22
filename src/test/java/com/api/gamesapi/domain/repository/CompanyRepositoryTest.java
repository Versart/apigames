package com.api.gamesapi.domain.repository;

import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.util.CompanyCreator;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("Save creates company when successful")
    void save_PersistCompany_WhenSuccessful() {
        Company companyToBeSaved = CompanyCreator.createCompanyToBeSaved();

        Company savedCompany = companyRepository.save(companyToBeSaved);

        Assertions.assertThat(savedCompany).isNotNull();

        Assertions.assertThat(savedCompany.getId()).isNotNull();

        Assertions.assertThat(savedCompany.getName()).isEqualTo(companyToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates company when successful")
    void save_UpdatesCompany_WhenSuccessful() {
        Company companyToBeSaved = CompanyCreator.createCompanyToBeSaved();

        Company savedCompany = companyRepository.save(companyToBeSaved);

        savedCompany.setName("Companhia Y teste");

        Company updatedCompany = companyRepository.save(savedCompany);

        Assertions.assertThat(updatedCompany).isNotNull();

        Assertions.assertThat(updatedCompany.getId()).isEqualTo(savedCompany.getId());

        Assertions.assertThat(updatedCompany.getName()).isEqualTo(savedCompany.getName());
    }

    @Test
    @DisplayName("Delete remove company when successful")
    void delete_RemoveCompany_WhenSuccessful() {
        Company companyToBeSaved = CompanyCreator.createCompanyToBeSaved();

        Company savedCompany = companyRepository.save(companyToBeSaved);

        companyRepository.delete(savedCompany);

        Optional<Company> companyOptional = companyRepository.findById(savedCompany.getId());

        Assertions.assertThat(companyOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name return Page of company when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Company companyToBeSaved = CompanyCreator.createCompanyToBeSaved();

        Company savedCompany = companyRepository.save(companyToBeSaved);

        Page<Company> companyPage = companyRepository.findByName(savedCompany.getName(), PageRequest.of(0, 10));

        Assertions.assertThat(companyPage).isNotNull();

        Assertions.assertThat(companyPage.getSize()).isPositive();

        Assertions.assertThat(companyPage).contains(savedCompany);
    }

    @Test
    @DisplayName("Find by name return List of company empty when no company is found")
    void findByName_ReturnsEmptyListOfAnime_WhenCompanyIsNotFound() {

        Page<Company> companyList = companyRepository.findByName("", PageRequest.of(0, 10));

        Assertions.assertThat(companyList).isNotNull().isEmpty();;
    }

    @Test
    @DisplayName("Save throw ConstraintValidationException when name is empty")
    void save_ThrowsConstraintException_WhenNameIsEmpty() {
        Company company = CompanyCreator.createCompanyWithoutName();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> companyRepository.save(company))
                    .withMessageContaining("must not be null");
    }

}