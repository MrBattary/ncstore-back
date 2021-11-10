package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;
import com.netcracker.ncstore.dto.request.SignInRequest;
import com.netcracker.ncstore.service.auth.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Auth controller which is responsible for:
 * authorization and authentication
 */
@RestController
public class AuthController {
    private final IAuthService authService;
    private final Logger log;

    /**
     * Constructor
     *
     * @param authService - IAuthService bean
     */
    public AuthController(final IAuthService authService) {
        this.authService = authService;
        this.log = LoggerFactory.getLogger(AuthController.class);
    }

    /**
     * Person sign up request
     * https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signUpPerson
     *
     * @param request - SignUpPersonRequest
     * @return - HTTP code
     */
    @RequestMapping(value = "/signup/person", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> signUpPerson(@RequestBody final SignUpPersonRequest request) {
        log.info("REQUEST: to signup " + request.getEmail() + " person");
        authService.signUpPerson(request);
        log.info("RESPONSE REQUEST: to signup " + request.getEmail() + " person");
        return ResponseEntity.ok().build();
    }

    /**
     * Company sign up request
     * https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signUpCompany
     *
     * @param request - SignUpCompanyRequest
     * @return - HTTP code
     */
    @RequestMapping(value = "/signup/company", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> signUpCompany(@RequestBody final SignUpCompanyRequest request) {
        log.info("REQUEST: to signup " + request.getEmail() + " company");
        authService.signUpCompany(request);
        log.info("RESPONSE REQUEST: to signup " + request.getEmail() + " company");
        return ResponseEntity.ok().build();
    }

    /**
     * Sign in request for any type of user
     * @param request - SignInRequest
     * @return - SignInResponse + HTTP code
     */
    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signIn
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SignInResponse> signIn(@RequestBody final SignInRequest request) {
        log.info("REQUEST: to signin " + request.getEmail() + " user");
        SignInResponse response = authService.signIn(request);
        log.info("RESPONSE REQUEST: to signin " + request.getEmail() + " user");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signOut
    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SignInResponse> signOut() {
        return null;
    }
}
