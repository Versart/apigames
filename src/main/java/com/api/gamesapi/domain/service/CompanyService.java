package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private GameService gameService;

    @Transactional
    public EntityModel<CompanyDTO> saveCompany(CompanyDTO companyDTO) {
        return companyMapper.toModel(companyRepository.save(companyMapper.toEntity(companyDTO)));
    }

    @Transactional
    public void deleteCompanyById(long companyId) {
        companyRepository.deleteById(companyId);
    }

    public EntityModel<CompanyDTO> searchCompanyById(Long companyId) {
        return companyRepository.findById(companyId).map(
                company -> {
                    return companyMapper.toModel(company);
                }
        ).orElseThrow(() -> new NotFoundException("Company not found!"));
    }

    public EntityModel<CompanyDTO> updateCompanyById(long companyId, CompanyDTO companyDTO) {
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

    public CollectionModel<EntityModel<CompanyDTO>> listCompanies() {
      return companyMapper.toModelList(companyRepository.findAll());
    }

    public CollectionModel<EntityModel<GameResponseDTO>> getGamesOfCompany(Long companyId) {
        if(companyExists(companyId)){
            return gameService.listGamesByCompanyId(companyId);
        }
        throw new CompanyNotFoundException("Company not found");
    }

}
