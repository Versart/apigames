package com.api.gamesapi.api.controller;

import com.api.gamesapi.domain.model.Company;
import com.api.gamesapi.domain.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Company> saveCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(companyService.saveCompany(company));
    }

    @GetMapping
    public List<Company> listCompanies() {
        return companyService.listCompanies();
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long companyId) {
        return companyService.searchCompanyById(companyId).map(
               company -> {
                  return ResponseEntity.ok(company);
               }
        ).orElse(
                ResponseEntity.notFound().build()
        );
    }

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
    }
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable long companyId){
        Optional<Company> companyDelete = companyService.searchCompanyById(companyId);
        if(companyDelete.isPresent()){
            companyService.deleteCompanyById(companyId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
