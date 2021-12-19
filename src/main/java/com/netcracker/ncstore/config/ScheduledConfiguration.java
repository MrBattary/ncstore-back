package com.netcracker.ncstore.config;

import com.netcracker.ncstore.repository.DiscountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Configuration
@EnableScheduling
@Slf4j
public class ScheduledConfiguration {
    private final DiscountRepository discountRepository;

    public ScheduledConfiguration(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    //@Scheduled(cron = "0 0 0/12 ? * *")
    @Scheduled(initialDelay = 0, fixedRate = 1000 * 60 * 60 * 12)
    @Transactional
    public void clearAllPastDiscounts() {
        int deleted = discountRepository.deleteDiscountByLessEndTime(Instant.now());
        discountRepository.flush();
        log.info("Deleted " + deleted + " past discounts from database");
    }
}
