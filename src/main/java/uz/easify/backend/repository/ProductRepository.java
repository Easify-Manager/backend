package uz.easify.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.easify.backend.domain.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity operations.
 * Provides comprehensive methods for product management and querying.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find a product by ID excluding deleted ones.
     */
    Optional<Product> findByIdAndDeletedFalse(Long id);

    /**
     * Find a product by SKU excluding deleted ones.
     */
    Optional<Product> findBySkuAndDeletedFalse(String sku);

    /**
     * Find all non-deleted products with pagination.
     */
    Page<Product> findByDeletedFalse(Pageable pageable);

    /**
     * Find all active, non-deleted products.
     */
    List<Product> findByActiveAndDeletedFalse(Boolean active);

    /**
     * Find products by category.
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.deleted = false")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Find featured products.
     */
    @Query("SELECT p FROM Product p WHERE p.featured = true AND p.active = true AND p.deleted = false")
    List<Product> findFeaturedProducts();

    /**
     * Search products by name or description.
     */
    @Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND p.deleted = false")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Search products by name or description with optional price filters.
     */
    @Query("SELECT p FROM Product p WHERE p.deleted = false AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> searchProductsWithPrice(@Param("searchTerm") String searchTerm,
                                          @Param("minPrice") BigDecimal minPrice,
                                          @Param("maxPrice") BigDecimal maxPrice,
                                          Pageable pageable);

    /**
     * Find products with low stock.
     */
    @Query("SELECT p FROM Product p JOIN p.inventory i " +
           "WHERE i.trackInventory = true " +
           "AND i.lowStockThreshold IS NOT NULL " +
           "AND (i.quantity - i.reservedQuantity) <= i.lowStockThreshold " +
           "AND p.active = true AND p.deleted = false")
    List<Product> findLowStockProducts();

    /**
     * Find out-of-stock products.
     */
    @Query("SELECT p FROM Product p JOIN p.inventory i " +
           "WHERE i.trackInventory = true " +
           "AND (i.quantity - i.reservedQuantity) <= 0 " +
           "AND p.active = true AND p.deleted = false")
    List<Product> findOutOfStockProducts();

    /**
     * Check if SKU already exists.
     */
    boolean existsBySkuAndDeletedFalse(String sku);

    /**
     * Check if SKU exists for a different product (for updates).
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p " +
           "WHERE p.sku = :sku AND p.id != :productId AND p.deleted = false")
    boolean existsBySkuAndIdNot(@Param("sku") String sku, @Param("productId") Long productId);
}
