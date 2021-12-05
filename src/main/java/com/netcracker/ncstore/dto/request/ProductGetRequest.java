package com.netcracker.ncstore.dto.request;

import com.netcracker.ncstore.util.enumeration.ESortOrder;
import com.netcracker.ncstore.util.enumeration.ESortRule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductGetRequest {
    private final List<UUID> categoriesIds;
    private final String searchText;
    private final int page;
    private final int size;
    private final Locale locale;
    private final ESortRule sort;
    private final ESortOrder sortOrder;
    private final UUID supplierId;
}
