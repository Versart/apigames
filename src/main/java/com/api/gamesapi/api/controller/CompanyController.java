package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO saveCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        return companyService.saveCompany(companyDTO);
    }

    @GetMapping
    public List<CompanyDTO> listCompanies() {
        return companyService.listCompanies();
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable long companyId) {
        return ResponseEntity.ok(companyService.searchCompanyById(companyId));
    }
    /*
    @PutMapping("/{companyId}")
    public ResponseEntity<Company> updateCompanyById(@PathVariable long companyId, @Valid @RequestBody Company company){
        return companyService.searchCompanyById(companyId).map(
                companyOld -> {
                    company.setId(companyOld.getId());
                    return ResponseEntity.ok(companyService.saveCompany(company));
                }
        ).orElse(
                ResponseEntity.notFound().build()
        );
    }*/
   /* @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable long companyId){
        Optional<Company> companyDelete = companyService.searchCompanyById(companyId);
        if(companyDelete.isPresent()){
            companyService.deleteCompanyById(companyId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }*/
}
