package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.api.SignInResponse;
import com.netcracker.ncstore.dto.api.SignRequest;
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
     * Sign up request
     *
     * @param request - email and password
     * @return - HTTP code
     */
    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signUp
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> signUp(@RequestBody final SignRequest request) {
        try {
            log.info("REQUEST: to signup " + request.getEmail() + " user");
            // TODO: Here should be something like: UserService.signUp(request);
            log.info("RESPONSE REQUEST: to signup " + request.getEmail() + " user");
            return ResponseEntity.ok().build();
            // TODO: Here should be another catch block for any error that returns 400
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signIn
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SignInResponse> signIn(@RequestBody final SignRequest request) {
        try {
            log.info("REQUEST: to signin " + request.getEmail() + " user");
            // TODO: Here should be something like: SignInResponse response = UserService.signIn(request);
            SignInResponse response = null;
            log.info("RESPONSE REQUEST: to signin " + request.getEmail() + " user");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
            // TODO: Here should be another catch block for any error that returns 400
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Authorization/signOut
    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SignInResponse> signOut() {
        return null;
    }
}
