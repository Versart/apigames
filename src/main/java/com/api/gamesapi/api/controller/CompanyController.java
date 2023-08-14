package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
=======
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
>>>>>>> hateoas
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/companies")
<<<<<<< HEAD
@AllArgsConstructor
public class CompanyController {


    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyDTO> saveCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        return new ResponseEntity<>(companyService.saveCompany(companyDTO),HttpStatus.CREATED);
=======
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class CompanyController {


    private final CompanyService companyService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a company",tags = "Company",
        responses = {@ApiResponse(description = "Successful operation", responseCode = "201"),
                     @ApiResponse(description = "When the body is invalid", responseCode = "400")})
    public EntityModel<CompanyResponse> saveCompany(@Valid @RequestBody CompanyRequest companyDTO) {
        return companyService.saveCompany(companyDTO);
>>>>>>> hateoas
    }

    @GetMapping
    @Operation(summary = "Lists all companies paginated" ,tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200")
    })
    public ResponseEntity<PagedModel<EntityModel<CompanyResponse>>> listCompanies(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(companyService.listCompanies(pageable));
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "Gets a company by id",tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the company does not exists in the database", responseCode = "404")})
    public ResponseEntity<EntityModel<CompanyResponse>> getCompanyById(@Schema(description = "this is company's id", type = "number") @PathVariable long companyId) {
        return ResponseEntity.ok(companyService.searchCompanyById(companyId));
    }
    @GetMapping("/find")
    @Operation(summary = "Gets companies which contains in the name, the name searched",tags = "Company"
    ,responses = {@ApiResponse(description = "Successful operation", responseCode = "200")})
    public ResponseEntity<CollectionModel<EntityModel<CompanyResponse>>> getByName(@Schema(description = "this is company's name", type = "string") @RequestParam String name){
        return ResponseEntity.ok(companyService.findCompanyByName(name));
    }

    @GetMapping("/{companyId}/games")
    @Operation(summary = "Lists all games of company",tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the company does not exists in the database", responseCode = "404")})
    public ResponseEntity<CollectionModel<EntityModel<GameResponseDTO>>> getGamesByCompanyId(@Schema(description = "this is company's id", type = "number") @PathVariable Long companyId ){
       return ResponseEntity.ok(companyService.getGamesOfCompany(companyId));
    }

    @PutMapping("/{companyId}")
    @Operation(summary = "Alters a company",tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the company does not exists in the database", responseCode = "404")
    })
    public ResponseEntity<EntityModel<CompanyResponse>> updateCompanyById(@Schema(description = "this is company's id", type = "number")@PathVariable long companyId, @Valid @RequestBody CompanyRequest companyDTO){
        return ResponseEntity.ok(companyService.updateCompanyById(companyId,companyDTO));
    }

    @DeleteMapping("/{companyId}")
    @Operation(summary = "Deletes a company by id",tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "204"),
        @ApiResponse(description = "When the company does not exists in the database", responseCode = "404")
    })
    public ResponseEntity<Void> deleteCompanyById(@Schema(description = "this is company's id", type = "number") @PathVariable long companyId){
        companyService.deleteCompanyById(companyId);
        return ResponseEntity.noContent().build();
    }
}
