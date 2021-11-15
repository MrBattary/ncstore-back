package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    /**
     * Finds all product by name using LIKE on provided string and also by using provided locale.
     * If no price for provided locale found, default one (en_US) is used.
     * MUST NOT BE USED FOR SORT AS NOT ALL TABLES ARE JOINED FOR BETTER PERFORMANCE
     *
     * @param name - string with must be in product name
     * @param locale - locale of product price
     * @param pageable - pageable
     * @return page of products
     */
    @Query("select p from Product p " +
            " left join p.productPrices pp " +
            " where " +
            " ((pp.locale='en_US' and not exists (select pp2 from ProductPrice pp2 where pp2.locale=?2 and pp2.product=pp.product)) or pp.locale=?2) " +
            " and (upper(p.name) like upper(concat('%', ?1, '%'))) ")
    Page<Product> findProductByLikeNameAndLocale(String name, Locale locale, Pageable pageable);

    /**
     * Exactly the same as findProductByLikeNameAndLocale method(above), but has joined Discount(add more joins here if present) entities
     * Because of joins, supports sorting using JpaSort.unsafe() in Pageable
     */
    @Query("select p from Product p " +
            " left join p.productPrices pp " +
            " left join pp.discount d " +
            " where " +
            "((pp.locale='en_US' and not exists (select pp2 from ProductPrice pp2 where pp2.locale=?2 and pp2.product=pp.product)) or pp.locale=?2) " +
            " and (upper(p.name) like upper(concat('%', ?1, '%'))) ")
    Page<Product> findProductByLikeNameAndLocaleWithJoins(String name, Locale locale, Pageable pageable);

    /**
     * Finds all product by name using LIKE on provided string and also by using provided locale.
     * If no price for provided locale found, default one (en_US) is used.
     * MUST NOT BE USED FOR SORT AS NOT ALL TABLES ARE JOINED FOR BETTER PERFORMANCE
     *
     * @param name - string with must be in product name
     * @param locale - locale of product price
     * @param categoriesIDs - list of UUIDs of categories
     * @param pageable - pageable
     * @return page of products
     */
    @Query("select p from Product p " +
            " join p.categories categories" +
            " left join p.productPrices pp " +
            " where " +
            " ((pp.locale='en_US' and not exists (select pp2 from ProductPrice pp2 where pp2.locale=?2 and pp2.product=pp.product)) or pp.locale=?2) " +
            " and (upper(p.name) like upper(concat('%', ?1, '%'))) " +
            " and (categories.id in ?3)")
    Page<Product> findProductsByLikeNameAndCategoriesAndLocale(String name, Locale locale, Collection<UUID> categoriesIDs, Pageable pageable);

    /**
     * Exactly the same as findProductsByLikeNameAndCategoriesAndLocale method(above), but has joined Discount(add more joins here if present) entities
     * Because of joins, supports sorting using JpaSort.unsafe() in Pageable
     */
    @Query("select p from Product p " +
            " join p.categories categories" +
            " left join p.productPrices pp " +
            " left join pp.discount d" +
            " where " +
            " ((pp.locale='en_US' and not exists (select pp2 from ProductPrice pp2 where pp2.locale=?2 and pp2.product=pp.product)) or pp.locale=?2) " +
            " and (upper(p.name) like upper(concat('%', ?1, '%'))) " +
            " and (categories.id in ?3) ")
    Page<Product> findProductsByLikeNameAndCategoriesWithJoins(String name, Locale locale, Collection<UUID> categoriesIDs, Pageable pageable);
}