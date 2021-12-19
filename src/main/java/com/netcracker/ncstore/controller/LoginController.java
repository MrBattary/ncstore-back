package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.request.SignInEmailPasswordBody;
import com.netcracker.ncstore.dto.request.SignInEmailPasswordRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;
import com.netcracker.ncstore.service.user.interfaces.web.ILoginWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/signin")
@Slf4j
public class LoginController {
    private final ILoginWebService loginWebService;

    public LoginController(final ILoginWebService loginWebService) {
        this.loginWebService = loginWebService;
    }


    @PostMapping
    public ResponseEntity<SignInResponse> signInUsingEmailPassword(@RequestBody final SignInEmailPasswordBody body) {
        log.info("REQUEST: to signin " + body.getEmail() + " user");

        SignInEmailPasswordRequest request = new SignInEmailPasswordRequest(
                body.getEmail(),
                body.getPassword()
        );

        SignInResponse response = loginWebService.signInByEmailAndPassword(request);

        log.info("RESPONSE: to signin " + body.getEmail() + " user");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
