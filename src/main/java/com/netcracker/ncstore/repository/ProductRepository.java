package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.repository.projections.ProductWithPriceInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    /**
     * Finds all product by name using LIKE on provided string and also by using provided locale.
     * If no price for provided locale found, default one (en_US) is used.
     *
     * @param name - string with must be in product name
     * @param locale - locale of product price
     * @param pageable - pageable
     * @return page of products
     */
    @Query("select p.id as id, p.name as name, pp as productPrices " +
            " from Product p " +
            " left join p.productPrices pp " +
            " left join pp.discount d" +
            " where " +
            " (upper(p.name) like upper(concat('%', ?1, '%'))) " +
            " and " +
            " ((pp.locale=?3 and not exists (select pp2 from ProductPrice pp2 where pp2.locale=?2 and pp2.product=pp.product)) or pp.locale=?2)")
    Page<ProductWithPriceInfo> findProductByLikeNameAndLocale(String name, Locale locale, Locale defaultLocale, Pageable pageable);

    /**
     * Finds all product by name using LIKE on provided string and also by using provided locale.
     * If no price for provided locale found, default one (en_US) is used.
     *
     * @param name - string with must be in product name
     * @param locale - locale of product price
     * @param categoriesIDs - list of UUIDs of categories
     * @param pageable - pageable
     * @return page of products
     */
    @Query("select p.id as id, p.name as name, pp as productPrices " +
            " from Product p " +
            " join p.categories categories" +
            " left join p.productPrices pp" +
            " left join pp.discount d " +
            " where " +
            " (upper(p.name) like upper(concat('%', ?1, '%'))) " +
            " and " +
            " ((pp.locale='en_US' and not exists (select pp2 from ProductPrice pp2 where pp2.locale=?2 and pp2.product=pp.product)) or pp.locale=?2) " +
            " and " +
            " (categories.id in ?3)")
    Page<ProductWithPriceInfo> findProductsByLikeNameAndCategoriesAndLocale(String name, Locale locale, Collection<UUID> categoriesIDs, Pageable pageable);

}