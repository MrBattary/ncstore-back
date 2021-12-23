package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductStatisticsReportDTO {
    private final long totalSalesAmount;
    private final double totalRevenue;
}
