package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    @Modifying
    @Query("delete from Discount d where d.endUtcTime < ?1")
    int deleteDiscountByLessEndTime(Instant endUtcTime);

}