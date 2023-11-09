package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.model.User;
import com.api.gamesapi.domain.service.UserService;
import com.api.gamesapi.infra.security.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Log in", tags = "Authentication", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200")
    })
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest){
        logger.info("Received request to login");
        var userNamePassword = new UsernamePasswordAuthenticationToken(loginRequest.getLogin(),loginRequest.getPassword());
        var auth = authenticationManager.authenticate(userNamePassword);
        var token = tokenService.getToken((User) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }

    
    @PostMapping("/register")
    @Operation(summary="Creates a new user", tags = "Authentication", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "201"),
        @ApiResponse(description = "When there is already user with the same email", responseCode = "409")
    })
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest userRequest) {
        logger.info("Received request to register user");
        return new ResponseEntity<>(userService.saveUser(userRequest), HttpStatus.CREATED);
    }
}
