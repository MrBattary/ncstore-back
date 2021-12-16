package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.response.PersonDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.PersonInfoResponse;
import com.netcracker.ncstore.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = "/person")
public class PersonController {
    private final IUserService userService;

    public PersonController(final IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/info")
    public ResponseEntity<PersonDetailedInfoResponse> getPersonInfo(Principal principal) {
        log.info("REQUEST: to get Person detailed info for user " + principal.getName());

        PersonDetailedInfoResponse response = userService.getDetailedPersonInfo(principal.getName());

        log.info("RESPONSE: to get Person detailed info for user " + principal.getName());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping(value = "/info")
    public ResponseEntity<?> updatePersonInfo(Principal principal) {
        return null;
    }

    @GetMapping(value = "/info/{userId}")
    public ResponseEntity<PersonInfoResponse> getConcretePersonInfo(@PathVariable final UUID userId) {
        log.info("REQUEST: to get Person info of user with UUID " + userId);

        PersonInfoResponse response = userService.getBasisPersonInfo(userId);

        log.info("RESPONSE: to get Person info of user with UUID " + userId);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
