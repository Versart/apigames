package com.api.gamesapi.util;

import com.api.gamesapi.domain.model.Company;

import java.time.LocalDate;

public class CompanyCreator {

    public static Company createCompanyToBeSaved() {
        return Company.builder().
                name("Companhia Teste X")
                .dateOfFoundation(LocalDate.now())
                .build();
    }

    public static Company createValidCompany() {
        return Company.builder()
                .id(1l)
                .name("Companhia Teste X")
                .dateOfFoundation(LocalDate.now())
                .build();
    }

    public static Company createCompanyWithoutName() {
        return Company.builder()
                .dateOfFoundation(LocalDate.now())
                .build();
    }
}
