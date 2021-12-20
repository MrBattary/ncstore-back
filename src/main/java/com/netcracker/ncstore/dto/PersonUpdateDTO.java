package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class PersonUpdateDTO {
    private final UUID userId;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
}
