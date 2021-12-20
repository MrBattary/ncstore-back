package com.netcracker.ncstore.service.review.interfaces;

import com.netcracker.ncstore.dto.request.ReviewCreateRequest;
import com.netcracker.ncstore.dto.request.ReviewGetRequest;
import com.netcracker.ncstore.dto.response.ReviewGetResponse;

import java.util.List;

/**
 * Interface for all WEB services related to reviews
 */
public interface IReviewWebService {
    List<ReviewGetResponse> getReviewsWithPagination(ReviewGetRequest request);

    ReviewGetResponse createNewReview(ReviewCreateRequest request);
}
