package com.api.gamesapi.api.controller;

import com.api.gamesapi.api.model.LoginRequest;
import com.api.gamesapi.api.model.UserRequest;
import com.api.gamesapi.api.model.UserResponse;
import com.api.gamesapi.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    @Operation(summary = "Log in", tags = "Authentication", responses = {
        @ApiResponse(description = "Successful operation", responseCode = "200"),
        @ApiResponse(description = "When login fails", responseCode = "401")
    })
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest){
        logger.info("Received request to login");
        return ResponseEntity.ok(userService.login(loginRequest));
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
