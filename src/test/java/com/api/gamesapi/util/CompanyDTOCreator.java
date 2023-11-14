package com.api.gamesapi.util;

import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDate;
import java.util.List;

public class CompanyDTOCreator {

    public static CompanyRequest createCompanyRequest() {
        return CompanyRequest.builder().name("Nintendo").dateOfFoundation(LocalDate.now()).build();
    }
    public static CompanyResponse createCompanyResponse() {
        return CompanyResponse.builder()
            .id(1l)
            .dateOfFoundation(LocalDate.now())
            .name("Nintendo").build();
    }
    public static CollectionModel<EntityModel<CompanyResponse>> createCollectionModelCompanyDTO() {
        return CollectionModel.of(List.of(createEntityModelCompanyResponse()));
    }
    public static PagedModel<EntityModel<CompanyResponse>> createPageDModelCompanyDTO() {
        List<EntityModel<CompanyResponse>> listCompanyDTO = List.of(createEntityModelCompanyResponse());
        PagedModel<EntityModel<CompanyResponse>> pagedModel = PagedModel.of(listCompanyDTO,
                new PagedModel.PageMetadata(1,1,1),Link.of("/companies/"));
        return pagedModel;
    }

    public static EntityModel<CompanyResponse> createEntityModelCompanyResponse() {
        return EntityModel.of(createCompanyResponse(),Link.of("companies/"));
    }







}
