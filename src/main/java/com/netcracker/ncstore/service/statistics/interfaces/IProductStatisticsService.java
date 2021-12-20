package com.netcracker.ncstore.service.statistics.interfaces;

import com.netcracker.ncstore.dto.ProductStatisticsReportDTO;

import java.util.UUID;

/**
 * Interface for all statistics services
 */
public interface IProductStatisticsService {
    /**
     * Returns metrics of specified product
     *
     * @param productId the UUID of product
     * @return ProductStatisticsReportDTO containing metrics
     */
    ProductStatisticsReportDTO getStatisticsReportForProduct(UUID productId);
}
