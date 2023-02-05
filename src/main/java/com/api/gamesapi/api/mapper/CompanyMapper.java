package com.api.gamesapi.api.mapper;

import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.model.Company;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CompanyDTO toModel(Company company) {
        return modelMapper.map(company,CompanyDTO.class);
    }

   public List<CompanyDTO> toModelList(List<Company> companies) {
        return companies.stream().map(
                company -> modelMapper.map(company,CompanyDTO.class)
        ).collect(Collectors.toList());
   }

   public Company toEntity(CompanyDTO companyDTO) {
        return modelMapper.map(companyDTO,Company.class);
   }
}
