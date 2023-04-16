package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.CompanyDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {


    private final CompanyService companyService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<CompanyDTO> saveCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        return companyService.saveCompany(companyDTO);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CompanyDTO>>> listCompanies() {
        return ResponseEntity.ok(companyService.listCompanies());
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<EntityModel<CompanyDTO>> getCompanyById(@PathVariable long companyId) {
        return ResponseEntity.ok(companyService.searchCompanyById(companyId));
    }
    @GetMapping("/find")
    public ResponseEntity<CollectionModel<EntityModel<CompanyDTO>>> getByName(@RequestParam String name){
        return ResponseEntity.ok(companyService.findCompanyByName(name));
    }

    @GetMapping("/{companyId}/games")
    public ResponseEntity<CollectionModel<EntityModel<GameResponseDTO>>> getGamesByCompanyId(@PathVariable Long companyId ){
       return ResponseEntity.ok(companyService.getGamesOfCompany(companyId));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<EntityModel<CompanyDTO>> updateCompanyById(@PathVariable long companyId, @Valid @RequestBody CompanyDTO companyDTO){
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
