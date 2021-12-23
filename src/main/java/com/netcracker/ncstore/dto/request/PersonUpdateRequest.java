package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class PersonUpdateRequest {
    private final String emailOfUser;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
}
