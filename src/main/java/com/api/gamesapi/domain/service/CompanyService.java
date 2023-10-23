package com.api.gamesapi.domain.service;

import com.api.gamesapi.api.mapper.CompanyMapper;
import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.repository.CompanyRepository;

import ch.qos.logback.classic.Logger;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Log4j2
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;


    private final GameService gameService;



    @Transactional
    public EntityModel<CompanyResponse> saveCompany(CompanyRequest companyDTO) {
        return companyMapper.toModel(companyRepository.save(companyMapper.toEntity(companyDTO)));
    }

    @Transactional
    public void deleteCompanyById(long companyId) {
        if(companyRepository.existsById(companyId))
            companyRepository.deleteById(companyId);
        else{
            throw new NotFoundException("Company not found!");
        }
    }

    public EntityModel<CompanyResponse> searchCompanyById(Long companyId) {
        return companyRepository.findById(companyId).map(
                     companyMapper::toModel
        ).orElseThrow(() -> new NotFoundException("Company not found!"));
    }

    public PagedModel<EntityModel<CompanyResponse>> findCompanyByName(String name, Pageable pageable){
        return companyMapper.toModelPage(companyRepository.findByNameContains(name, pageable));
    }

    public EntityModel<CompanyResponse> updateCompanyById(long companyId, CompanyRequest companyDTO) {
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

    public PagedModel<EntityModel<CompanyResponse>> listCompanies(Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(pageable);
        return companyMapper.toModelPage(companyPage);
    }

    public PagedModel<EntityModel<GameResponseDTO>> getGamesOfCompany(Long companyId, Pageable pageable) {
        if(companyExists(companyId)){
            return gameService.listGamesByCompanyId(companyId, pageable);
        }
        throw new CompanyNotFoundException("Company not found");
    }

}
