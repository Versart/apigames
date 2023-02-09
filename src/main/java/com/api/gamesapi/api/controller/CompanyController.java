package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.domain.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CompanyDTO>> listCompanies() {

        return ResponseEntity.ok(companyService.listCompanies());
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable long companyId) {
        return ResponseEntity.ok(companyService.searchCompanyById(companyId));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyDTO> updateCompanyById(@PathVariable long companyId, @Valid @RequestBody CompanyDTO companyDTO){
        return ResponseEntity.ok(companyService.updateCompanyById(companyId,companyDTO));
    }
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable long companyId){
        boolean companyExists = companyService.companyExists(companyId);
        if(companyExists){
            companyService.deleteCompanyById(companyId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
