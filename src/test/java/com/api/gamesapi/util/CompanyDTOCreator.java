package com.api.gamesapi.util;

import com.api.gamesapi.api.model.CompanyDTO;

import java.time.LocalDate;

public class CompanyDTOCreator {

    public static CompanyDTO createCompany() {
        return CompanyDTO.builder().name("Company Test").dateOfFoundation(LocalDate.now()).build();
    }




}
