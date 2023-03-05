package com.api.gamesapi.api.mapper;

import com.api.gamesapi.api.controller.CompanyController;
import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.model.Company;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class CompanyMapper {

    @Autowired
    private ModelMapper modelMapper;

    public EntityModel<CompanyDTO> toModel(Company company) {

        return EntityModel.of(modelMapper.map(company,CompanyDTO.class)
                ,linkTo(methodOn(CompanyController.class).getCompanyById(company.getId())).withSelfRel()
                ,linkTo(methodOn(CompanyController.class).listCompanies()).withRel("All Companies")
                );
    }

   public CollectionModel<EntityModel<CompanyDTO>> toModelList(List<Company> companies) {
        return CollectionModel.of(companies.stream().map(
                this::toModel
        ).collect(Collectors.toList()),linkTo(methodOn(CompanyController.class).listCompanies()).withSelfRel());
   }

   public Company toEntity(CompanyDTO companyDTO) {
        return modelMapper.map(companyDTO,Company.class);
   }
}
