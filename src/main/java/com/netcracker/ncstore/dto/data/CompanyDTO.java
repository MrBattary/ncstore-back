package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Contains all information about real Company entity,
 * as it is represented in database.
 * <p>
 * Used to safely transfer entity data between parts of the Program
 */
@Getter
@AllArgsConstructor
public class CompanyDTO {
    private final UUID userId;
    private final String companyName;
    private final String description;
    private final LocalDate foundationDate;

    public CompanyDTO(Company company) {
        userId=company.getUserId();
        companyName = company.getCompanyName();
        description = company.getDescription();
        foundationDate = company.getFoundationDate();
    }
}
