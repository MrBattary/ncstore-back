package com.netcracker.ncstore.service.review.interfaces;

import com.netcracker.ncstore.dto.ReviewCreateDTO;
import com.netcracker.ncstore.exception.ReviewServiceValidationException;
import com.netcracker.ncstore.model.ProductReview;

/**
 * Interface for all business services that work with reviews
 */
public interface IReviewBusinessService {
    ProductReview createReview(ReviewCreateDTO createDTO) throws ReviewServiceValidationException;
}
