package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;


    @Transactional
    public CompanyDTO saveCompany(CompanyDTO companyDTO) {
        return companyMapper.toModel(companyRepository.save(companyMapper.toEntity(companyDTO)));
    }

    @Transactional
    public void deleteCompanyById(long companyId) {
        companyRepository.deleteById(companyId);
    }

    public CompanyDTO searchCompanyById(Long companyId) {
        return companyRepository.findById(companyId).map(
                company -> {
                    return companyMapper.toModel(company);
                }
        ).orElseThrow(() -> new NotFoundException("Company not found!"));
    }

    public CompanyDTO updateCompanyById(long companyId, CompanyDTO companyDTO) {
        return companyRepository.findById(companyId).map(
                company -> {
                    company.setName(companyDTO.getName());
                    company.setDateOfFoundation(companyDTO.getDateOfFoundation());
                    return companyMapper.toModel(companyRepository.save(company));
                }
        ).orElseThrow(() -> new NotFoundException("Company not found"));
    }

    public boolean companyExists(Long companyId) {
        if(companyRepository.existsById(companyId))
            return true;

        throw new NotFoundException("Company not found");
    }

    public List<CompanyDTO> listCompanies() {
      return companyMapper.toModelList(companyRepository.findAll());
    }

}
