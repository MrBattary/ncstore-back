package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.body.CompanyUpdateBody;
import com.netcracker.ncstore.dto.request.CompanyDetailedInfoRequest;
import com.netcracker.ncstore.dto.request.CompanyUpdateRequest;
import com.netcracker.ncstore.dto.response.CompanyDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyUpdateResponse;
import com.netcracker.ncstore.service.user.interfaces.web.ICompanyWebService;
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
@RequestMapping(value = "/company")
public class CompanyController {
    private final ICompanyWebService companyWebService;

    public CompanyController(ICompanyWebService companyWebService) {
        this.companyWebService = companyWebService;
    }


    @GetMapping(value = "/info")
    public ResponseEntity<CompanyDetailedInfoResponse> getCompanyInfo(final Principal principal) {
        log.info("REQUEST: to get self Company info for user " + principal.getName());

        CompanyDetailedInfoRequest request = new CompanyDetailedInfoRequest(
                principal.getName(),
                principal.getName()
        );

        CompanyDetailedInfoResponse response = companyWebService.getDetailedCompanyInfo(request);

        log.info("RESPONSE: to get self Company info for user " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }

    @PutMapping(value = "/info")
    public ResponseEntity<?> updateCompanyInfo(@RequestBody final CompanyUpdateBody body,
                                               final Principal principal) {
        log.info("REQUEST: to update Company info for user " + principal.getName());

        CompanyUpdateRequest request = new CompanyUpdateRequest(
                principal.getName(),
                body.getCompanyName(),
                body.getDescription(),
                body.getFoundationDate()
        );

        CompanyUpdateResponse response = companyWebService.updateCompanyInfo(request);

        log.info("RESPONSE: to update Company info for user " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }

    @GetMapping(value = "/info/{userId}")
    public ResponseEntity<CompanyInfoResponse> getConcreteCompanyInfo(@PathVariable final UUID userId) {
        log.info("REQUEST: to get Company info about user with UUID " + userId);

        CompanyInfoResponse response = companyWebService.getPublicCompanyInfo(userId);

        log.info("RESPONSE: to get Company info about user with UUID " + userId);

        return ResponseEntity.
                ok(response);
    }
}
