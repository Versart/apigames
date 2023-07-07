package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.CompanyService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class CompanyController {


    private final CompanyService companyService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<CompanyResponse> saveCompany(@Valid @RequestBody CompanyRequest companyDTO) {
        return companyService.saveCompany(companyDTO);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CompanyResponse>>> listCompanies(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(companyService.listCompanies(pageable));
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<EntityModel<CompanyResponse>> getCompanyById(@PathVariable long companyId) {
        return ResponseEntity.ok(companyService.searchCompanyById(companyId));
    }
    @GetMapping("/find")
    public ResponseEntity<CollectionModel<EntityModel<CompanyResponse>>> getByName(@RequestParam String name){
        return ResponseEntity.ok(companyService.findCompanyByName(name));
    }

    @GetMapping("/{companyId}/games")
    public ResponseEntity<CollectionModel<EntityModel<GameResponseDTO>>> getGamesByCompanyId(@PathVariable Long companyId ){
       return ResponseEntity.ok(companyService.getGamesOfCompany(companyId));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<EntityModel<CompanyResponse>> updateCompanyById(@PathVariable long companyId, @Valid @RequestBody CompanyRequest companyDTO){
        return ResponseEntity.ok(companyService.updateCompanyById(companyId,companyDTO));
    }
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable long companyId){
        companyService.deleteCompanyById(companyId);
        return ResponseEntity.noContent().build();
    }
}
