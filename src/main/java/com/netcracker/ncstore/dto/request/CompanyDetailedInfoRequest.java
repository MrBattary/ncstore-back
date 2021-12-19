package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CompanyDetailedInfoRequest {
    private final String emailOfCompany;
    private final String emailOfIssuer;
}
