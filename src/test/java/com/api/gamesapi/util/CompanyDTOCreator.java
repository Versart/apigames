package com.api.gamesapi.util;

import com.api.gamesapi.api.model.CompanyDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDate;
import java.util.List;

public class CompanyDTOCreator {

    public static CompanyDTO createCompanyDTO() {
        return CompanyDTO.builder().name("Company Test").dateOfFoundation(LocalDate.now()).build();
    }



    public static PagedModel<EntityModel<CompanyDTO>> createPageDModelCompanyDTO() {
        List<EntityModel<CompanyDTO>> listCompanyDTO = List.of(EntityModel.of(createCompanyDTO()));
        PagedModel<EntityModel<CompanyDTO>> pagedModel = PagedModel.of(listCompanyDTO,
                new PagedModel.PageMetadata(1,1,1));
        return pagedModel;
    }

    public static EntityModel<CompanyDTO> createEntityModelCompanyDTO() {
        return EntityModel.of(createCompanyDTO());
    }






}
