package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.body.SignUpCompanyBody;
import com.netcracker.ncstore.dto.body.SignUpPersonBody;
import com.netcracker.ncstore.dto.request.RegisterCompanyRequest;
import com.netcracker.ncstore.dto.request.RegisterPersonRequest;
import com.netcracker.ncstore.service.web.register.IRegistrationWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/signup")
@Slf4j
public class RegistrationController {
    private final IRegistrationWebService registrationWebService;

    public RegistrationController(final IRegistrationWebService registrationWebService) {
        this.registrationWebService = registrationWebService;
    }

    @PostMapping(value = "/person")
    public ResponseEntity<?> signUpPerson(@RequestBody final SignUpPersonBody body) {
        log.info("REQUEST: to signup " + body.getEmail() + " person");

        RegisterPersonRequest request = new RegisterPersonRequest(
                body.getEmail(),
                body.getPassword(),
                body.getNickName(),
                body.getFirstName(),
                body.getLastName(),
                body.getBirthday(),
                body.getRoles()
        );

        registrationWebService.registerPerson(request);

        log.info("RESPONSE REQUEST: to signup " + body.getEmail() + " person");

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/company")
    public ResponseEntity<?> signUpCompany(@RequestBody final SignUpCompanyBody body) {
        log.info("REQUEST: to signup " + body.getEmail() + " company");

        RegisterCompanyRequest request = new RegisterCompanyRequest(
                body.getEmail(),
                body.getPassword(),
                body.getCompanyName(),
                body.getFoundationDate(),
                body.getRoles()
        );

        registrationWebService.registerCompany(request);

        log.info("RESPONSE REQUEST: to signup " + body.getEmail() + " company");

        return ResponseEntity.noContent().build();
    }
}
