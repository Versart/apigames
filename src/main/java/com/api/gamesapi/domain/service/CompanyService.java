package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CompanyService {

    Logger logger = LogManager.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final GameService gameService;

    @Transactional
    public EntityModel<CompanyResponse> saveCompany(CompanyRequest companyDTO) {
        logger.info("Creating new company");
        return companyMapper.toModel(companyRepository.save(companyMapper.toEntity(companyDTO)));
    }

    @Transactional
    public void deleteCompanyById(long companyId) {
        logger.info("Deleting company with id {}", companyId);
        if(companyRepository.existsById(companyId))
            companyRepository.deleteById(companyId);
        else{
            throw new NotFoundException(String.format("Company not found with id %d", companyId));
        }
    }

    public EntityModel<CompanyResponse> searchCompanyById(Long companyId) {
        logger.info("Fetching company with id {}", companyId);
        return companyRepository.findById(companyId).map(
                     companyMapper::toModel
        ).orElseThrow(() -> new NotFoundException(String.format("Company not found with id %d", companyId)));
    }

    public PagedModel<EntityModel<CompanyResponse>> findCompanyByName(String name, Pageable pageable){
        logger.info("Fetching company which contains {} in the name", name);
        return companyMapper.toModelPage(companyRepository.findByNameContains(name, pageable));
    }

    public EntityModel<CompanyResponse> updateCompanyById(long companyId, CompanyRequest companyDTO) {
        logger.info("Updating company with id {}", companyId);
        return companyRepository.findById(companyId).map(
                company -> {
                    company.setName(companyDTO.getName());
                    company.setDateOfFoundation(companyDTO.getDateOfFoundation());
                    return companyMapper.toModel(companyRepository.save(company));
                }
        ).orElseThrow(() -> new NotFoundException(String.format("Company not found with id %d", companyId)));
    }

    public boolean companyExists(Long companyId) {
        logger.info("Verifying if exists company with id {}", companyId);
        if(companyRepository.existsById(companyId))
            return true;
        throw new NotFoundException(String.format("Company not found with id %d", companyId));
    }

    public PagedModel<EntityModel<CompanyResponse>> listCompanies(Pageable pageable) {
        logger.info("Fetching all companies");
        Page<Company> companyPage = companyRepository.findAll(pageable);
        return companyMapper.toModelPage(companyPage);
    }

    public PagedModel<EntityModel<GameResponseDTO>> getGamesOfCompany(Long companyId, Pageable pageable) {
        logger.info("Fetching all games of the company with id {}", companyId);
        if(companyExists(companyId)){
            return gameService.listGamesByCompanyId(companyId, pageable);
        }
        throw new CompanyNotFoundException(String.format("Company not found with id %d", companyId));
    }

}
