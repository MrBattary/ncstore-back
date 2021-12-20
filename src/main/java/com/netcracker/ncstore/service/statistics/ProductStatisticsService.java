package com.netcracker.ncstore.service.statistics;

import com.netcracker.ncstore.dto.ProductStatisticsReportDTO;
import com.netcracker.ncstore.repository.OrderItemRepository;
import com.netcracker.ncstore.service.statistics.interfaces.IProductStatisticsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductStatisticsService implements IProductStatisticsService {
    private final OrderItemRepository orderItemRepository;

    public ProductStatisticsService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public ProductStatisticsReportDTO getStatisticsReportForProduct(UUID productId) {
        long totalAmount = orderItemRepository.countProductOccurrences(productId);
        double totalSales = orderItemRepository.getTotalUcSalesForProduct(productId);

        return new ProductStatisticsReportDTO(
                totalAmount,
                totalSales
        );
    }
}
