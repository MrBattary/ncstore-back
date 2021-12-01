package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.repository.projections.ProductWithPriceInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    /**
     * Finds all product by name using LIKE on provided string
     *
     * @param name     - string with must be in product name
     * @param pageable - pageable
     * @return page of products
     */
    @Query("select p " +
            " from Product p " +
            " left join p.productPrices pp " +
            " left join pp.discount d" +
            " where " +
            " (upper(p.name) like upper(concat('%', ?1, '%'))) ")
    Page<Product> findProductByLikeNameAndLocale(String name, Pageable pageable);

    /**
     * Finds all product by name using LIKE on provided string
     * and by provided Categories list
     *
     * @param name          - string with must be in product name
     * @param categoriesIDs - list of UUIDs of categories
     * @param pageable      - pageable
     * @return page of products
     */
    @Query("select p " +
            " from Product p " +
            " join p.categories categories" +
            " left join p.productPrices pp" +
            " left join pp.discount d " +
            " where " +
            " (upper(p.name) like upper(concat('%', ?1, '%'))) " +
            " and " +
            " (categories.id in ?2)")
    Page<Product> findProductsByLikeNameAndCategoriesAndLocale(String name, Collection<UUID> categoriesIDs, Pageable pageable);


    /**
     * Finds all products created by user with provided ID,
     * by name using LIKE on provided string
     *
     * @param name     - string with must be in product name
     * @param pageable - pageable
     * @return page of products
     */
    @Query("select p " +
            " from Product p " +
            " left join p.productPrices pp " +
            " left join pp.discount d" +
            " where " +
            " p.supplier.id=?1" +
            " and " +
            " (upper(p.name) like upper(concat('%', ?2, '%'))) ")
    Page<Product> findProductByUserIdAndByLikeNameAndLocale(UUID userID, String name, Pageable pageable);

    /**
     * Finds all products created by user with provided ID,
     * by name using LIKE on provided string
     * and by provided Categories list
     *
     * @param name          - string with must be in product name
     * @param categoriesIDs - list of UUIDs of categories
     * @param pageable      - pageable
     * @return page of products
     */
    @Query("select p " +
            " from Product p " +
            " join p.categories categories" +
            " left join p.productPrices pp" +
            " left join pp.discount d " +
            " where " +
            " p.supplier.id=?1" +
            " and " +
            " (upper(p.name) like upper(concat('%', ?2, '%'))) " +
            " and " +
            " (categories.id in ?3)")
    Page<Product> findProductsUserIdAndByLikeNameAndCategoriesAndLocale(UUID userID, String name, Collection<UUID> categoriesIDs, Pageable pageable);

    void deleteProductById(UUID id);
}