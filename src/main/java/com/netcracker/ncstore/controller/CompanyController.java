package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.request.CompanyDetailedInfoRequest;
import com.netcracker.ncstore.dto.response.CompanyDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyInfoResponse;
import com.netcracker.ncstore.service.user.interfaces.web.ICompanyWebService;
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
@RequestMapping(value = "/company")
public class CompanyController {
    private final ICompanyWebService companyWebService;

    public CompanyController(ICompanyWebService companyWebService) {
        this.companyWebService = companyWebService;
    }


    @GetMapping(value = "/info")
    public ResponseEntity<CompanyDetailedInfoResponse> getCompanyInfo(Principal principal) {
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

    @PostMapping(value = "/info")
    public ResponseEntity<?> updateCompanyInfo(Principal principal) {
        return null;
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
