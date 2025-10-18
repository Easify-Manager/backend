package uz.easify.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.easify.backend.domain.entity.ProductImage;

/**
 * Repository interface for ProductImage entity operations.
 */
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    /**
     * Find all images for a product.
     */
    List<ProductImage> findByProductIdAndDeletedFalseOrderByDisplayOrderAsc(Long productId);

    /**
     * Find primary image for a product.
     */
    ProductImage findByProductIdAndIsPrimaryTrueAndDeletedFalse(Long productId);

    /**
     * Delete all images for a product.
     */
    void deleteByProductId(Long productId);
}
