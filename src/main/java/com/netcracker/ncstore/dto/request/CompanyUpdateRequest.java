package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CompanyUpdateRequest {
    private final String emailOfUser;
    private final String companyName;
    private final String description;
    private final LocalDate foundationDate;
}
