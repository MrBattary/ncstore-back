package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PersonDetailedInfoRequest {
    private final String emailOfPerson;
    private final String emailOfIssuer;
}
