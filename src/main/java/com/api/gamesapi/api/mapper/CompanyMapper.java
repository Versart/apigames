package com.api.gamesapi.api.mapper;

import com.api.gamesapi.api.controller.CompanyController;
import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.domain.model.Company;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
@RequiredArgsConstructor
public class CompanyMapper {


    private final ModelMapper modelMapper;


    private final PagedResourcesAssembler<Company> pagedResourcesAssembler;

    public EntityModel<CompanyResponse> toModel(Company company) {

        return EntityModel.of(modelMapper.map(company,CompanyResponse.class)
                ,linkTo(methodOn(CompanyController.class).getCompanyById(company.getId())).withSelfRel()
                ,linkTo(methodOn(CompanyController.class).listCompanies(PageRequest.of(0, 10))).withRel("All Companies")
                );
    }

   public CollectionModel<EntityModel<CompanyResponse>> toModelList(List<Company> companies) {
        return CollectionModel.of(companies.stream().map(
                this::toModel
        ).collect(Collectors.toList())
                ,linkTo(methodOn(CompanyController.class).listCompanies(PageRequest.of(0, 10))).withSelfRel()
                 );
   }

    public PagedModel<EntityModel<CompanyResponse>> toModelPage(Page pageCompany) {

        return pagedResourcesAssembler.toModel(pageCompany,this::toModel);

    }

   public Company toEntity(CompanyRequest companyRequest) {
        return modelMapper.map(companyRequest,Company.class);
   }
}
