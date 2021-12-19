package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.request.PersonDetailedInfoRequest;
import com.netcracker.ncstore.dto.response.PersonDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.PersonInfoResponse;
import com.netcracker.ncstore.service.user.interfaces.web.IPersonWebService;
import lombok.extern.slf4j.Slf4j;
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
    private final IPersonWebService personWebService;

    public PersonController(IPersonWebService personWebService) {
        this.personWebService = personWebService;
    }

    @GetMapping(value = "/info")
    public ResponseEntity<PersonDetailedInfoResponse> getPersonInfo(Principal principal) {
        log.info("REQUEST: to get self Person info for user " + principal.getName());

        PersonDetailedInfoRequest request = new PersonDetailedInfoRequest(
                principal.getName(),
                principal.getName()
        );

        PersonDetailedInfoResponse response = personWebService.getDetailedPersonInfo(request);

        log.info("RESPONSE: to get self Person info for user " + principal.getName());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/info")
    public ResponseEntity<?> updatePersonInfo(Principal principal) {
        return null;
    }

    @GetMapping(value = "/info/{userId}")
    public ResponseEntity<PersonInfoResponse> getConcretePersonInfo(@PathVariable final UUID userId) {
        log.info("REQUEST: to get Person info of user with UUID " + userId);

        PersonInfoResponse response = personWebService.getPublicPersonInfo(userId);

        log.info("RESPONSE: to get Person info of user with UUID " + userId);

        return ResponseEntity.ok(response);
    }
}
