package com.netcracker.ncstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductGetStatisticsResponse {
    private final long totalSalesAmount;
    private final double totalEarnings;
}
