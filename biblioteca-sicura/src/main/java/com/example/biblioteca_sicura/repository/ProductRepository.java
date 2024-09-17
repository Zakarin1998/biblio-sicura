package com.example.biblioteca_sicura.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.biblioteca_sicura.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Find a product by its SKU
    Optional<Product> findBySku(String sku);
    // Find products by name
    Set<Product> findByNameContainingIgnoreCase(String name);
    // Find products by category ID (if you want to search by category)
    Set<Product> findByCategoriesId(Long categoryId);

    // Example with custom query
        // Example of a custom JPQL query to find products by name and price range
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.price BETWEEN :minPrice AND :maxPrice")
    Set<Product> findProductsByNameAndPriceRange(
        @Param("name") String name,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice
    );
}
