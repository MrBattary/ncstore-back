package com.netcracker.ncstore.service.review.interfaces;

import com.netcracker.ncstore.dto.ReviewPageRequestDTO;
import com.netcracker.ncstore.model.ProductReview;
import org.springframework.data.domain.Page;

public interface IReviewDataService {
    Page<ProductReview> getPageOfReviews(ReviewPageRequestDTO pageRequestDTO);
}
