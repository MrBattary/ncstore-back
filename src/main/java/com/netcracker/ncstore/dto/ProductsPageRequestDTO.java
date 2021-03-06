package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.util.enumeration.ESortOrder;
import com.netcracker.ncstore.util.enumeration.EProductSortRule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductsPageRequestDTO {
    private final List<String> categoriesNames;
    private final String searchText;
    private final int page;
    private final int size;
    private final Locale locale;
    private final EProductSortRule sort;
    private final ESortOrder sortOrder;
    private final UUID supplierId;
}
