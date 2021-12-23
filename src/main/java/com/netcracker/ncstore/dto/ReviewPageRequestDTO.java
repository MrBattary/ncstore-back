package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.util.enumeration.EReviewSortRule;
import com.netcracker.ncstore.util.enumeration.ESortOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewPageRequestDTO {
    private final int page;
    private final int size;
    private final EReviewSortRule reviewSortRule;
    private final ESortOrder sortOrder;
}
