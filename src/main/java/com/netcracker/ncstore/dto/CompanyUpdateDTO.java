package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CompanyUpdateDTO {
    private final UUID userId;
    private final String companyName;
    private final String description;
    private final LocalDate foundationDate;
}
