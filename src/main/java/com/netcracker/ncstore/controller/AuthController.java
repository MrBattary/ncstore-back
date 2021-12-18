package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.request.SignInRequest;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;
import com.netcracker.ncstore.service.auth.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Auth controller which is responsible for:
 * authorization and authentication
 */
@RestController
@Slf4j
public class AuthController {
    private final IAuthService authService;

    public AuthController(final IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/signup/person")
    public ResponseEntity<?> signUpPerson(@RequestBody final SignUpPersonRequest request) {
        log.info("REQUEST: to signup " + request.getEmail() + " person");

        authService.signUpPerson(request);

        log.info("RESPONSE REQUEST: to signup " + request.getEmail() + " person");

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/signup/company")
    public ResponseEntity<?> signUpCompany(@RequestBody final SignUpCompanyRequest request) {
        log.info("REQUEST: to signup " + request.getEmail() + " company");

        authService.signUpCompany(request);

        log.info("RESPONSE REQUEST: to signup " + request.getEmail() + " company");

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody final SignInRequest request) {
        log.info("REQUEST: to signin " + request.getEmail() + " user");

        SignInResponse response = authService.signIn(request);

        log.info("RESPONSE REQUEST: to signin " + request.getEmail() + " user");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
