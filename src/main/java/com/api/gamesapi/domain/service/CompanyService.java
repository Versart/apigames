package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;


    @Transactional
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompanyById(long companyId) {
        companyRepository.deleteById(companyId);
    }

    public Optional<Company> searchCompanyById(long companyId) {
        return companyRepository.findById(companyId);
    }

    public List<CompanyDTO> listCompanies() {
      return companyMapper.toModelList(companyRepository.findAll());
    }

}
