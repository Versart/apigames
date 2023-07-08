package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "Saves a company",tags = "Company")
    public EntityModel<CompanyResponse> saveCompany(@Valid @RequestBody CompanyRequest companyDTO) {
        return companyService.saveCompany(companyDTO);
    }

    @GetMapping
    @Operation(summary = "Lists all companies paginated" ,tags = "Company")
    public ResponseEntity<PagedModel<EntityModel<CompanyResponse>>> listCompanies(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(companyService.listCompanies(pageable));
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "Gets a company by id",tags = "Company")
    public ResponseEntity<EntityModel<CompanyResponse>> getCompanyById(@Schema(description = "this is company's id", type = "long") @PathVariable long companyId) {
        return ResponseEntity.ok(companyService.searchCompanyById(companyId));
    }
    @GetMapping("/find")
    @Operation(summary = "Gets companies which contains in the name, the name searched",tags = "Company")
    public ResponseEntity<CollectionModel<EntityModel<CompanyResponse>>> getByName(@Schema(description = "this is company's name", type = "long") @RequestParam String name){
        return ResponseEntity.ok(companyService.findCompanyByName(name));
    }

    @GetMapping("/{companyId}/games")
    @Operation(summary = "Lists all games of company",tags = "Company")
    public ResponseEntity<CollectionModel<EntityModel<GameResponseDTO>>> getGamesByCompanyId(@Schema(description = "this is company's id", type = "long") @PathVariable Long companyId ){
       return ResponseEntity.ok(companyService.getGamesOfCompany(companyId));
    }

    @PutMapping("/{companyId}")
    @Operation(summary = "Alters a company",tags = "Company")
    public ResponseEntity<EntityModel<CompanyResponse>> updateCompanyById(@Schema(description = "this is company's id", type = "long")@PathVariable long companyId, @Valid @RequestBody CompanyRequest companyDTO){
        return ResponseEntity.ok(companyService.updateCompanyById(companyId,companyDTO));
    }
    @DeleteMapping("/{companyId}")
    @Operation(summary = "Deletes a company by id",tags = "Company")
    public ResponseEntity<Void> deleteCompanyById(@Schema(description = "this is company's id", type = "long") @PathVariable long companyId){
        companyService.deleteCompanyById(companyId);
        return ResponseEntity.noContent().build();
    }
}
