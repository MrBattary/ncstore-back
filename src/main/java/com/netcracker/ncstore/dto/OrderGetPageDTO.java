package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@Getter
public class OrderGetPageDTO {
    private final Pageable pageable;
    private final String email;
}
