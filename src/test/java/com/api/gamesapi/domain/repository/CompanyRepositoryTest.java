package com.api.gamesapi.domain.repository;

import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.model.Company;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;



   /* public CompanyDTO createCompany() {
        return new CompanyDTO().builder().dateOfFoundation(LocalDate.now()).name("Test company").build();
    }*/
    @Test
    public void save_ThrowsConstraintException_WhenNameIsEmpty() {
        Company company = new Company();
        company.setDateOfFoundation(LocalDate.now());
       /* Assertions.assertThatThrownBy( () -> companyRepository.save(company))
                .isInstanceOf(ConstraintViolationException.class);*/
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> companyRepository.save(company)).withMessageContaining("não deve ser nulo");
    }
}