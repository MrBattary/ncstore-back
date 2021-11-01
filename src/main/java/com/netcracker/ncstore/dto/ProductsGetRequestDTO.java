package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductsGetRequestDTO {
    private final List<UUID> categoriesIds;
    private final String searchText;
    private final int page;
    private final int size;
    private final Locale locale;
}
