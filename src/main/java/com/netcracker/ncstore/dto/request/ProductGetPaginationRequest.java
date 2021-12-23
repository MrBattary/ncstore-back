package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductGetPaginationRequest {
    private final List<String> categoriesNames;
    private final String searchText;
    private final int page;
    private final int size;
    private final Locale locale;
    private final String sort;
    private final String sortOrder;
    private final UUID supplierId;
}
