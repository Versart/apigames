package com.api.gamesapi.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class CompanyDTO {


    private String name;

    private LocalDate dateOfFoundation;
}
