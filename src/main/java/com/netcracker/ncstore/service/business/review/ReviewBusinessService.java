package com.netcracker.ncstore.service.business.review;

import com.netcracker.ncstore.config.event.customevent.ProductReviewedEvent;
import com.netcracker.ncstore.dto.ReviewCreateDTO;
import com.netcracker.ncstore.exception.ReviewServiceValidationException;
import com.netcracker.ncstore.model.ProductReview;
import com.netcracker.ncstore.repository.ProductReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Slf4j
public class ReviewBusinessService implements IReviewBusinessService {
    private final ProductReviewRepository productReviewRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public ReviewBusinessService(final ProductReviewRepository productReviewRepository,
                                 final ApplicationEventPublisher applicationEventPublisher) {
        this.productReviewRepository = productReviewRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public ProductReview createReview(ReviewCreateDTO createDTO) throws ReviewServiceValidationException {
        if (createDTO.getRating() <= 0 || createDTO.getRating() > 5) {
            throw new ReviewServiceValidationException("Review rating must be integer in bounds [1;5]. ");
        }
        if (createDTO.getProduct() == null) {
            throw new ReviewServiceValidationException("Review must have product. ");
        }
        if (createDTO.getAuthor() == null) {
            throw new ReviewServiceValidationException("Review must have author. ");
        }

/*        if (productReviewRepository.existsByAuthorId(createDTO.getAuthor().getId())) {
            throw new ReviewServiceValidationException("This user already reviewed this product. ");
        }*/

        ProductReview review = productReviewRepository.save(
                new ProductReview(
                        Instant.now(),
                        createDTO.getRating(),
                        createDTO.getText(),
                        createDTO.getProduct(),
                        createDTO.getAuthor()
                )
        );

        applicationEventPublisher.publishEvent(
                new ProductReviewedEvent(
                        this,
                        createDTO.getProduct().getId(),
                        createDTO.getRating()
                )
        );

        return review;
    }
}
