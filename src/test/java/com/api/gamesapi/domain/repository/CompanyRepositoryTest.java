package com.api.gamesapi.domain.repository;

import com.api.gamesapi.domain.model.Company;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;



    public Company createCompany() {
        return Company.builder().name("Companhia Z teste")
                .dateOfFoundation(LocalDate.now())
                .build();
    }
    @Test
    @DisplayName("Save creates company when successful")
    public void save_PersistCompany_WhenSuccessful() {
        Company companyToBeSaved = createCompany();
        Company savedCompany = this.companyRepository.save(companyToBeSaved);
        Assertions.assertThat(savedCompany).isNotNull();
        Assertions.assertThat(savedCompany.getId()).isNotNull();
        Assertions.assertThat(savedCompany.getName()).isEqualTo(companyToBeSaved.getName());
    }
    @Test
    public void save_ThrowsConstraintException_WhenNameIsEmpty() {
        Company company = new Company();
        company.setDateOfFoundation(LocalDate.now());
       /* Assertions.assertThatThrownBy( () -> companyRepository.save(company))
                .isInstanceOf(ConstraintViolationException.class);*/
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> companyRepository.save(company)).withMessageContaining("must not be null");
    }
}