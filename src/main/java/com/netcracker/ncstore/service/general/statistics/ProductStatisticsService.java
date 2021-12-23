package com.netcracker.ncstore.service.general.statistics;

import com.google.inject.internal.util.ImmutableMap;
import com.netcracker.ncstore.config.event.customevent.ProductOrderCompletedEvent;
import com.netcracker.ncstore.config.event.customevent.ProductReviewedEvent;
import com.netcracker.ncstore.dto.ProductStatisticsReportDTO;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.ProductStatistic;
import com.netcracker.ncstore.repository.OrderItemRepository;
import com.netcracker.ncstore.repository.ProductStatisticRepository;
import com.netcracker.ncstore.service.data.product.IProductDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ProductStatisticsService implements IProductStatisticsService {
    private final OrderItemRepository orderItemRepository;
    private final ProductStatisticRepository statisticRepository;
    private final IProductDataService productDataService;

    private final ConcurrentHashMap<UUID, Integer> concurrentProductIdOrderCountMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, ArrayDeque<Integer>> concurrentProductIdRatingsMap = new ConcurrentHashMap<>();

    public ProductStatisticsService(final OrderItemRepository orderItemRepository,
                                    final ProductStatisticRepository statisticRepository,
                                    final IProductDataService productDataService) {
        this.orderItemRepository = orderItemRepository;
        this.statisticRepository = statisticRepository;
        this.productDataService = productDataService;
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

    @Async
    @EventListener
    public void handleItemBoughtEvent(ProductOrderCompletedEvent event) {
        UUID productId = event.getProductId();

        concurrentProductIdOrderCountMap.merge(
                productId,
                1,
                Integer::sum);
    }

    @Async
    @EventListener
    public void handleProductReviewedEvent(ProductReviewedEvent event) {
        UUID productId = event.getProductId();
        int rating = event.getRating();



        concurrentProductIdRatingsMap.merge(
                productId,
                new ArrayDeque<>(Arrays.asList(rating)),
                (oldVal, newVal) -> {
                    newVal.addAll(oldVal);
                    return newVal;
                }
        );
    }

    @Scheduled(fixedDelay = 1000 * 10)
    @Transactional
    public void addCollectedStatisticToDatabase() {
        Map<UUID, Integer> copyOfOrderMap = ImmutableMap.copyOf(concurrentProductIdOrderCountMap);
        concurrentProductIdOrderCountMap.keySet().removeAll(copyOfOrderMap.keySet());

        Map<UUID, ArrayDeque<Integer>> copyOfRatingMap = ImmutableMap.copyOf(concurrentProductIdRatingsMap);
        concurrentProductIdRatingsMap.keySet().removeAll(copyOfRatingMap.keySet());

        for (Map.Entry<UUID, Integer> entry : copyOfOrderMap.entrySet()) {
            UUID productId = entry.getKey();
            Product product = productDataService.getProductById(productId);
            int orderCount = entry.getValue();

            ProductStatistic productStatistic = statisticRepository.findById(productId).orElse(null);

            if (productStatistic == null) {
                productStatistic = new ProductStatistic(product);
                productStatistic.setTotalSalesAmount(orderCount);
                statisticRepository.save(productStatistic);
            } else {
                productStatistic.setTotalSalesAmount(productStatistic.getTotalSalesAmount() + orderCount);
            }

            log.info("Updated statistic for product with UUID "+productId+" new total sales count: "+productStatistic.getTotalSalesAmount());

        }

        for(Map.Entry<UUID, ArrayDeque<Integer>> entry: copyOfRatingMap.entrySet()){
            UUID productId = entry.getKey();
            Product product = productDataService.getProductById(productId);
            ArrayDeque<Integer> deque = entry.getValue();

            ProductStatistic productStatistic = statisticRepository.findById(productId).orElse(null);

            if(productStatistic==null){
                productStatistic = new ProductStatistic(product);
                statisticRepository.save(productStatistic);
            }

            while (!deque.isEmpty()){
                int rating = deque.pop();
                productStatistic.setTotalReviewCount(productStatistic.getTotalReviewCount()+1);

                if(rating==1){
                    productStatistic.setOneStarReviewCount(productStatistic.getOneStarReviewCount()+1);
                }else if(rating==2){
                    productStatistic.setTwoStarsReviewCount(productStatistic.getTwoStarsReviewCount()+1);
                }else if(rating==3){
                    productStatistic.setThreeStarsReviewCount(productStatistic.getThreeStarsReviewCount()+1);
                }else if(rating==4){
                    productStatistic.setFourStarsReviewCount(productStatistic.getFourStarsReviewCount()+1);
                }else if(rating==5){
                    productStatistic.setFiveStarsReviewCount(productStatistic.getFiveStarsReviewCount()+1);
                }
            }
            productStatistic.calculateAverageRating();

            log.info("Updated statistic for product with UUID "+productId+" new avg rating: "+productStatistic.getAverageRating());
        }
    }
}
