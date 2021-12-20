package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.body.ReviewCreateBody;
import com.netcracker.ncstore.dto.request.ReviewCreateRequest;
import com.netcracker.ncstore.dto.request.ReviewGetRequest;
import com.netcracker.ncstore.dto.response.ReviewGetResponse;
import com.netcracker.ncstore.service.review.interfaces.IReviewWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/review")
@Slf4j
public class ReviewController {
    private final IReviewWebService reviewWebService;

    public ReviewController(IReviewWebService reviewWebService) {
        this.reviewWebService = reviewWebService;
    }


    @GetMapping
    public ResponseEntity<List<ReviewGetResponse>> getReviewForProductWithPagination(
            @RequestParam final int page,
            @RequestParam final int size,
            @RequestParam final UUID productId,
            @RequestParam(required = false, defaultValue = "DATE") final String sort,
            @RequestParam(required = false, defaultValue = "DESC") final String sortOrder) {

        log.info("REQUEST: to get reviews for product with UUID " + productId + " on page: " + page + " with size: " + size + " using sort: " + sort + " " + sortOrder);
        ReviewGetRequest request = new ReviewGetRequest(
                page,
                size,
                productId,
                sort,
                sortOrder
        );

        List<ReviewGetResponse> response = reviewWebService.getReviewsWithPagination(request);

        log.info("RESPONSE: to get reviews for product with UUID " + productId + " on page: " + page + " with size: " + size + " using sort: " + sort + " " + sortOrder);

        return ResponseEntity.
                ok().
                body(response);
    }

    @PostMapping
    ResponseEntity<ReviewGetResponse> createNewReview(@RequestBody final ReviewCreateBody body,
                                                      final Principal principal) {

        log.info("REQUEST: to create new review for product with UUID " + body.getProductId() + " with rating "+body.getRating() + " and author with email "+principal.getName());

        ReviewCreateRequest request = new ReviewCreateRequest(
                principal.getName(),
                body.getProductId(),
                body.getRating(),
                body.getReviewText()
        );

        ReviewGetResponse response = reviewWebService.createNewReview(request);

        log.info("RESPONSE: to create new review for product with UUID " + body.getProductId() + " with rating "+body.getRating() + " and author with email "+principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }
}
