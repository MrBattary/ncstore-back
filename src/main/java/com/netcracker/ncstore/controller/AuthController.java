package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;
import com.netcracker.ncstore.dto.request.SignInRequest;
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
    private final Logger log;

    /**
     * Constructor
     * <p>
     * TODO: In the future, any services should be the arguments of constructor
     */
    public AuthController() {
        this.log = LoggerFactory.getLogger(AuthController.class);
    }

    /**
     * Person sign up request
     *
     * @param request - SignUpPersonRequest
     * @return - HTTP code
     */
    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signUpPerson
    @RequestMapping(value = "/signup/person", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> signUpPerson(@RequestBody SignUpPersonRequest request) {
        try {
            log.info("REQUEST: to signup " + request.getEmail() + " person");
            // TODO: Here should be something like: UserService.signUp(request);
            log.info("RESPONSE REQUEST: to signup " + request.getEmail() + " person");
            return ResponseEntity.ok().build();
            // TODO: Here should be another catch block for any error that returns 400
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Company sign up request
     *
     * @param request - SignUpCompanyRequest
     * @return - HTTP code
     */
    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signUpCompany
    @RequestMapping(value = "/signup/company", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> signUpCompany(@RequestBody SignUpCompanyRequest request) {
        log.info("REQUEST: to signup " + request.getEmail() + " company");
        // TODO: Here should be something like: UserService.signUp(request);
        log.info("RESPONSE REQUEST: to signup " + request.getEmail() + " company");
        return ResponseEntity.ok().build();
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signIn
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SignInResponse> signIn(@RequestBody final SignInRequest request) {
        log.info("REQUEST: to signin " + request.getEmail() + " user");
        // TODO: Here should be something like: SignInResponse response = UserService.signIn(request);
        SignInResponse response = null;
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
