package com.netcracker.ncstore.service.review;

import com.netcracker.ncstore.dto.ReviewCreateDTO;
import com.netcracker.ncstore.dto.ReviewPageRequestDTO;
import com.netcracker.ncstore.dto.request.ReviewCreateRequest;
import com.netcracker.ncstore.dto.request.ReviewGetRequest;
import com.netcracker.ncstore.dto.response.ReviewGetResponse;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.exception.ReviewServiceValidationException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.ProductReview;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.service.product.interfaces.IProductDataService;
import com.netcracker.ncstore.service.review.interfaces.IReviewBusinessService;
import com.netcracker.ncstore.service.review.interfaces.IReviewDataService;
import com.netcracker.ncstore.service.review.interfaces.IReviewWebService;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import com.netcracker.ncstore.util.converter.ReviewRequestConverter;
import com.netcracker.ncstore.util.enumeration.EReviewSortRule;
import com.netcracker.ncstore.util.enumeration.ESortOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewWebService implements IReviewWebService {
    private final IReviewBusinessService reviewBusinessService;
    private final IReviewDataService reviewDataService;
    private final IUserDataService userDataService;
    private final IProductDataService productDataService;

    public ReviewWebService(final IReviewBusinessService reviewBusinessService,
                            final IReviewDataService reviewDataService,
                            final IUserDataService userDataService,
                            final IProductDataService productDataService) {
        this.reviewBusinessService = reviewBusinessService;
        this.reviewDataService = reviewDataService;
        this.userDataService = userDataService;
        this.productDataService = productDataService;
    }

    @Override
    public List<ReviewGetResponse> getReviewsWithPagination(ReviewGetRequest request) {
        EReviewSortRule sortRule = ReviewRequestConverter.convertSortRuleStringToEnum(request.getSort());
        ESortOrder sortOrder = ReviewRequestConverter.convertSortOrderStringToEnum(request.getSortOrder());

        ReviewPageRequestDTO pageRequestDTO = new ReviewPageRequestDTO(
                request.getPage(),
                request.getSize(),
                sortRule,
                sortOrder
        );

        Page<ProductReview> page = reviewDataService.getPageOfReviews(pageRequestDTO);

        return page.
                getContent().
                stream().
                map(
                        e -> new ReviewGetResponse(
                                e.getId(),
                                e.getProduct().getId(),
                                e.getAuthor().getId(),
                                userDataService.getPublicNameForUser(e.getAuthor().getId()),
                                e.getCreationTimeUtc(),
                                e.getProductRating(),
                                e.getReviewText()
                        )
                ).collect(Collectors.toList());
    }

    @Override
    public ReviewGetResponse createNewReview(ReviewCreateRequest request) {
        try {
            User author = userDataService.getUserByEmail(request.getEmailOfAuthor());

            Product product = productDataService.getProductById(request.getProductId());

            ReviewCreateDTO createDTO = new ReviewCreateDTO(
                    author,
                    product,
                    request.getRating(),
                    request.getText()
            );

            ProductReview productReview = reviewBusinessService.createReview(createDTO);

            return new ReviewGetResponse(
                    productReview.getId(),
                    productReview.getProduct().getId(),
                    productReview.getAuthor().getId(),
                    userDataService.getPublicNameForUser(productReview.getAuthor().getId()),
                    productReview.getCreationTimeUtc(),
                    productReview.getProductRating(),
                    productReview.getReviewText()
            );
        }catch (UserServiceNotFoundException | ProductServiceNotFoundException notFoundException){
            throw new GeneralPermissionDeniedException(notFoundException.getMessage(), notFoundException);
        }catch (ReviewServiceValidationException validationException){
            throw new GeneralBadRequestException(validationException.getMessage(), validationException);
        }
    }
}
