package com.netcracker.ncstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @GetMapping(value = "/info")
    public ResponseEntity<?> getCompanyInfo(Principal principal) {
        return null;
    }

    @PostMapping(value = "/info")
    public ResponseEntity<?> updateCompanyInfo(Principal principal) {
        return null;
    }

    @GetMapping(value = "/info/{userId}")
    public ResponseEntity<?> getConcreteCompanyInfo(@PathVariable final String userId,
                                                    Principal principal) {
        return null;
    }
}
