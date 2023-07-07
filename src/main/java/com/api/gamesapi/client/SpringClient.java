package com.api.gamesapi.client;

import com.api.gamesapi.api.model.CompanyRequest;
import com.api.gamesapi.api.model.GameRequestDTO;
import com.api.gamesapi.api.model.GameResponseDTO;
import com.api.gamesapi.domain.model.Game;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;

@Log4j2
public class SpringClient {

    public static void main(String[] args) {
//        ResponseEntity<EntityModel<GameResponseDTO>> game = new RestTemplate()
//                .getForEntity("http://localhost:8080/games/6",
//                        new ParameterizedTypeReference<EntityModel<GameResponseDTO>>() {
//                        });

        ResponseEntity<EntityModel<GameResponseDTO>> game = new RestTemplate()
                .exchange("http://localhost:8080/games/6", HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {});
        log.info(game.getBody());

        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Sega");
        companyDTO.setDateOfFoundation(LocalDate.of(1982,5,27));
        /*CompanyDTO companySaved = new RestTemplate()
                .postForObject("http://localhost:8080/companies",companyDTO,
                        CompanyDTO.class);
        log.info(companySaved);*/
       /*ResponseEntity<EntityModel<CompanyDTO>> companySaved =
        new RestTemplate().exchange("http://localhost:8080/companies", HttpMethod.POST,
                new HttpEntity<>(companyDTO),
                new ParameterizedTypeReference<EntityModel<CompanyDTO>>() {
                });
        log.info(companySaved);*/
        companyDTO.setName("Eletronic Arts");
        ResponseEntity<EntityModel<CompanyRequest>> companyUpdated = new RestTemplate().exchange("http://localhost:8080/companies/15", HttpMethod.PUT,
                new HttpEntity<>(companyDTO), new ParameterizedTypeReference<EntityModel<CompanyRequest>>() {
                }
        );
        log.info(companyUpdated);
        ResponseEntity<Void> companyDeleted = new RestTemplate().exchange("http://localhost:8080/companies/15", HttpMethod.DELETE
        ,null,Void.class);
        log.info(companyDeleted);
    }
}
