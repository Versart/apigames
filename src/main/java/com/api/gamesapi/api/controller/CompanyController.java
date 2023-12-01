package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.CompanyResponse;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
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

    Logger logger = LogManager.getLogger(CompanyController.class);

    private final CompanyService companyService;

    @PostMapping
    @Operation(summary = "Creates a new company",tags = "Company",
        responses = {@ApiResponse(description = "Successful operation", responseCode = "201"),
                     @ApiResponse(description = "When the body is invalid", responseCode = "400"),
                     @ApiResponse(description = "When the user is not authenticated", responseCode = "401"),
                     @ApiResponse(description = "When the user does not have permission", responseCode = "403")})
    public ResponseEntity<EntityModel<CompanyResponse>> saveCompany(@Valid @RequestBody CompanyRequest companyDTO) {
        logger.info("Received request to create new company");
        return new ResponseEntity<>(companyService.saveCompany(companyDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lists all companies paginated" ,tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the user is not authenticated", responseCode = "401"),
        @ApiResponse(description = "When the user does not have permission", responseCode = "403")
    })
    public ResponseEntity<PagedModel<EntityModel<CompanyResponse>>> listCompanies(@ParameterObject Pageable pageable) {
        logger.info("Received request to fetch all companies");
        return ResponseEntity.ok(companyService.listCompanies(pageable));
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "Gets a company by id",tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the company does not exists in the database", responseCode = "404"),
        @ApiResponse(description = "When the user is not authenticated", responseCode = "401"),
        @ApiResponse(description = "When the user does not have permission", responseCode = "403")
    })
    public ResponseEntity<EntityModel<CompanyResponse>> getCompanyById(@Schema(description = "this is company's id", type = "number") @PathVariable long companyId) {
        logger.info("Received request to fetch  company with id {}", companyId);
        return ResponseEntity.ok(companyService.searchCompanyById(companyId));
    }
    @GetMapping("/find")
    @Operation(summary = "Gets companies which contains in the name, the name searched",tags = "Company"
    ,responses = {@ApiResponse(description = "Successful operation", responseCode = "200"),
                  @ApiResponse(description = "When the user is not authenticated", responseCode = "401"),
                  @ApiResponse(description = "When the user does not have permission", responseCode = "403")})
    public ResponseEntity<PagedModel<EntityModel<CompanyResponse>>> getByName(@Schema(description = "this is company's name", type = "string") @RequestParam String name, @ParameterObject Pageable pageable){
        logger.info("Received request to fetch companies which contains {} in the name", name);
        return ResponseEntity.ok(companyService.findCompanyByName(name, pageable));
    }

    @GetMapping("/{companyId}/games")
    @Operation(summary = "Lists all games of company",tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the company does not exists in the database", responseCode = "404"),
        @ApiResponse(description = "When the user is not authenticated", responseCode = "401"),
        @ApiResponse(description = "When the user does not have permission", responseCode = "403")})
    public ResponseEntity<PagedModel<EntityModel<GameResponseDTO>>> getGamesByCompanyId(@Schema(description = "this is company's id", type = "number") @PathVariable Long companyId, @ParameterObject Pageable pageable ){
        logger.info("Received request to fetch all games of the company with id {}", companyId);
        return ResponseEntity.ok(companyService.getGamesOfCompany(companyId, pageable));
    }

    @PutMapping("/{companyId}")
    @Operation(summary = "Alters a company",tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When the company does not exists in the database", responseCode = "404"),
        @ApiResponse(description = "When the user is not authenticated", responseCode = "401"),
        @ApiResponse(description = "When the user does not have permission", responseCode = "403")
    })
    public ResponseEntity<EntityModel<CompanyResponse>> updateCompanyById(@Schema(description = "this is company's id", type = "number")@PathVariable long companyId, @Valid @RequestBody CompanyRequest companyDTO){
        logger.info("Received request to update company with id {}", companyId);
        return ResponseEntity.ok(companyService.updateCompanyById(companyId,companyDTO));
    }

    @DeleteMapping("/{companyId}")
    @Operation(summary = "Deletes a company by id",tags = "Company", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "204"),
        @ApiResponse(description = "When the company does not exists in the database", responseCode = "404"),
        @ApiResponse(description = "When the user is not authenticated", responseCode = "401"),
        @ApiResponse(description = "When the user does not have permission", responseCode = "403")
    })
    public ResponseEntity<Void> deleteCompanyById(@Schema(description = "this is company's id", type = "number") @PathVariable long companyId){
        logger.info("Received request to delete company with id {}", companyId);
        companyService.deleteCompanyById(companyId);
        return ResponseEntity.noContent().build();
    }
}
