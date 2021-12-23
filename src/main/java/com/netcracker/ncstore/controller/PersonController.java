package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.body.PersonUpdateBody;
import com.netcracker.ncstore.dto.request.PersonDetailedInfoRequest;
import com.netcracker.ncstore.dto.request.PersonUpdateRequest;
import com.netcracker.ncstore.dto.response.PersonDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.PersonInfoResponse;
import com.netcracker.ncstore.dto.response.PersonUpdateResponse;
import com.netcracker.ncstore.service.web.person.IPersonWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<PersonDetailedInfoResponse> getPersonInfo(final Principal principal) {
        log.info("REQUEST: to get self Person info for user " + principal.getName());

        PersonDetailedInfoRequest request = new PersonDetailedInfoRequest(
                principal.getName(),
                principal.getName()
        );

        PersonDetailedInfoResponse response = personWebService.getDetailedPersonInfo(request);

        log.info("RESPONSE: to get self Person info for user " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }

    @PutMapping(value = "/info")
    public ResponseEntity<?> updatePersonInfo(@RequestBody final PersonUpdateBody body,
                                              final Principal principal) {
        log.info("REQUEST: to update Person info for user " + principal.getName());

        PersonUpdateRequest request = new PersonUpdateRequest(
                principal.getName(),
                body.getNickName(),
                body.getFirstName(),
                body.getLastName(),
                body.getBirthday()
        );

        PersonUpdateResponse response = personWebService.updatePersonInfo(request);

        log.info("RESPONSE: to update Person info for user " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }

    @GetMapping(value = "/info/{userId}")
    public ResponseEntity<PersonInfoResponse> getConcretePersonInfo(@PathVariable final UUID userId) {
        log.info("REQUEST: to get Person info of user with UUID " + userId);

        PersonInfoResponse response = personWebService.getPublicPersonInfo(userId);

        log.info("RESPONSE: to get Person info of user with UUID " + userId);

        return ResponseEntity.ok(response);
    }
}
