package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public class UserBalanceGetRequest {
    private final String emailOfUser;
    private final String emailOfIssuer;
    private final Locale locale;
}
