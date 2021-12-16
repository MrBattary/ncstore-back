package com.netcracker.ncstore.repository.specification;

import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.model.Category_;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.model.ProductPrice_;
import com.netcracker.ncstore.model.Product_;
import com.netcracker.ncstore.model.User_;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.util.enumeration.ESortOrder;
import com.netcracker.ncstore.util.enumeration.ESortRule;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public abstract class ProductSpecifications {

    /**
     * Filters products by LIKE with provided string
     *
     * @param name string to be used in LIKE
     * @return Specification<Product>
     */
    public static Specification<Product> getByLikeName(String name) {
        return (root, query, criteriaBuilder) -> {
            String namePattern = "%" + name.toUpperCase(Locale.ROOT) + "%";
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(Product_.name)), namePattern);
        };
    }

    /**
     * Filters products that belongs to one of the specified categories
     *
     * @param categoriesNames list of categories
     * @return Specification<Product>
     */
    public static Specification<Product> getByCategoriesNames(List<String> categoriesNames) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(categoriesNames)) {
                return criteriaBuilder.and();
            } else {
                Subquery<Category> categorySubquery = query.subquery(Category.class);
                Root<Category> categoryRoot = categorySubquery.from(Category.class);
                Join<Category, Product> categoryProductJoin = categoryRoot.join(Category_.products);
                categorySubquery.select(categoryRoot).where(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get(Product_.id), categoryProductJoin.get(Product_.id)),
                                categoryRoot.get(Category_.name).in(categoriesNames)
                        )
                );
                return criteriaBuilder.exists(categorySubquery);
            }
        };
    }

    /**
     * Filters products that belongs to supplier with provided ID
     *
     * @param supplierId the UUID of supplier
     * @return Specification<Product>
     */
    public static Specification<Product> getBySupplierId(UUID supplierId) {
        return (root, query, criteriaBuilder) -> {
            if (supplierId == null) {
                return criteriaBuilder.and();
            } else {
                return criteriaBuilder.equal(root.get(Product_.SUPPLIER).get(User_.ID), supplierId);
            }
        };
    }

    /**
     * Filters products which have provided EProductStatus
     *
     * @param productStatus status
     * @return Specification<Product>
     */
    public static Specification<Product> getByProductStatus(EProductStatus productStatus) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.productStatus), productStatus);
    }

    /**
     * Orders products. Method is huge and needs Locales to prevent products duplications when using ordering by price or discount,
     * as this types of ordering require joins
     *
     * @param order         the order (asc, desc)
     * @param rule          rule according to which products will be ordered (name, price, etc.)
     * @param targetLocale  locale that we want product to have (the locale of user who made a request)
     * @param defaultLocale default locale used in application
     * @return Specification<Product>
     */
    public static Specification<Product> order(ESortOrder order, ESortRule rule, Locale targetLocale, Locale defaultLocale) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //null because should be initialized only when it is needed. Useless join every time is not good for performance.
                Join<Product, ProductPrice> productPriceJoin = null;

                //by default always true, but is used when some duplicates must be cleaned up after joins.
                //add more statements by using criteriaBuilder.and()
                Predicate returnPredicate = criteriaBuilder.and();
                Path<?> criteriaOrderingPath;

                //add here more rules if they need Join<Product, ProductPrice>
                if (rule.equals(ESortRule.PRICE) || rule.equals(ESortRule.DISCOUNT)) {
                    Subquery<ProductPrice> targetLocaleExistsSubquery = query.subquery(ProductPrice.class);
                    Root<ProductPrice> productPriceRoot = targetLocaleExistsSubquery.from(ProductPrice.class);
                    productPriceJoin = root.join(Product_.productPrices, JoinType.LEFT);

                    targetLocaleExistsSubquery.select(productPriceRoot).
                            where(
                                    criteriaBuilder.and(
                                            criteriaBuilder.equal(productPriceRoot.get(ProductPrice_.LOCALE), targetLocale),
                                            criteriaBuilder.equal(productPriceRoot.get(ProductPrice_.PRODUCT), productPriceJoin.get(ProductPrice_.PRODUCT))
                                    )
                            );

                    Predicate defaultLocalePredicate = criteriaBuilder.equal(
                            productPriceJoin.get(ProductPrice_.LOCALE),
                            defaultLocale
                    );

                    Predicate targetLocalePredicate = criteriaBuilder.equal(
                            productPriceJoin.get(ProductPrice_.LOCALE),
                            targetLocale
                    );

                    Predicate targetLocaleNotExists = criteriaBuilder.not(
                            criteriaBuilder.exists(targetLocaleExistsSubquery)
                    );

                    returnPredicate = criteriaBuilder.or(
                            criteriaBuilder.and(
                                    defaultLocalePredicate,
                                    targetLocaleNotExists),
                            targetLocalePredicate
                    );
                }

                //more IFs and criteriaBuilder.and() for returnPredicate if needed


                switch (rule) {
                    default:
                    case DEFAULT:
                        criteriaOrderingPath = root.get(Product_.name);
                        break;
                    case POPULAR:
                        criteriaOrderingPath = root.get(Product_.name);//TODO
                        break;
                    case RATING:
                        criteriaOrderingPath = root.get(Product_.name);//TODO
                        break;
                    case PRICE:
                        criteriaOrderingPath = productPriceJoin.get(ProductPrice_.priceWithDiscount);
                        break;
                    case DATE:
                        criteriaOrderingPath = root.get(Product_.creationUtcTime);
                        break;
                    case DISCOUNT:
                        criteriaOrderingPath = productPriceJoin.get(ProductPrice_.discountPercent);
                        break;
                }


                switch (order) {
                    default:
                    case ASC:
                        query.orderBy(criteriaBuilder.asc(criteriaOrderingPath));
                        break;
                    case DESC:
                        query.orderBy(criteriaBuilder.desc(criteriaOrderingPath));
                        break;
                    case RAND:
                        query.orderBy(criteriaBuilder.asc(criteriaBuilder.function("random", null)));
                        break;
                }

                return returnPredicate;

            }
        };
    }
}
