package com.api.gamesapi.util;

import com.api.gamesapi.api.model.CompanyDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDate;
import java.util.List;

public class CompanyDTOCreator {

    public static CompanyDTO createCompany() {
        return CompanyDTO.builder().name("Company Test").dateOfFoundation(LocalDate.now()).build();
    }



    public static PagedModel<EntityModel<CompanyDTO>> getPageDModelCompanyDTO() {
        List<EntityModel<CompanyDTO>> listCompanyDTO = List.of(EntityModel.of(createCompany()));
        PagedModel<EntityModel<CompanyDTO>> pagedModel = PagedModel.of(listCompanyDTO,
                new PagedModel.PageMetadata(1,1,1));
        return pagedModel;
    }






}
