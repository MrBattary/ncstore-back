package com.netcracker.ncstore.service.review;

import com.netcracker.ncstore.dto.ReviewPageRequestDTO;
import com.netcracker.ncstore.model.ProductReview;
import com.netcracker.ncstore.model.ProductReview_;
import com.netcracker.ncstore.repository.ProductReviewRepository;
import com.netcracker.ncstore.service.review.interfaces.IReviewDataService;
import com.netcracker.ncstore.util.enumeration.EReviewSortRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReviewDataService implements IReviewDataService {
    private final ProductReviewRepository productReviewRepository;

    public ReviewDataService(ProductReviewRepository productReviewRepository) {
        this.productReviewRepository = productReviewRepository;
    }

    @Override
    public Page<ProductReview> getPageOfReviews(ReviewPageRequestDTO pageRequestDTO) {

        Sort sort;

        switch (pageRequestDTO.getSortOrder()){
            default:
            case ASC:
                sort = Sort.by(Sort.Order.asc(getColumnNameToSortBySortEnum(pageRequestDTO.getReviewSortRule())));
                break;
            case DESC:
                sort = Sort.by(Sort.Order.desc(getColumnNameToSortBySortEnum(pageRequestDTO.getReviewSortRule())));
                break;
        }

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage(),
                pageRequestDTO.getSize(),
                sort
        );

        return productReviewRepository.findAll(pageable);
    }

    private String getColumnNameToSortBySortEnum(EReviewSortRule sortRule){
        switch (sortRule){
            default:
            case DATE:
                return ProductReview_.CREATION_TIME_UTC;
            case RATING:
                return ProductReview_.PRODUCT_RATING;
        }
    }
}
