package com.netcracker.ncstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User controller which is responsible for requests/responses
 * which are related to the user's accounts
 */
@RestController
public class UserController {
    private final Logger log;

    /**
     * Constructor
     *
     * TODO: In the future, any services should be the arguments of constructor
     */
    public UserController() {
        this.log = LoggerFactory.getLogger(UserController.class);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/User/getUser
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getOwnUserInfo() {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/User/changeUser
    @RequestMapping(value = "/user/info", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> changeOwnUserInfo() {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/User/getUserProfile
    @RequestMapping(value = "/user/info/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo(@PathVariable final String userId) {
        return null;
    }
}
