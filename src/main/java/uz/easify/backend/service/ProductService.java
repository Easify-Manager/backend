package uz.easify.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import uz.easify.backend.dto.request.ProductRequest;
import uz.easify.backend.dto.response.ProductResponse;

/**
 * Service interface for product management operations.
 */
public interface ProductService {

    /**
     * Create a new product.
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * Update an existing product.
     */
    ProductResponse updateProduct(Long id, ProductRequest request);

    /**
     * Get a product by ID.
     */
    ProductResponse getProductById(Long id);

    /**
     * Get a product by SKU.
     */
    ProductResponse getProductBySku(String sku);

    /**
     * Get all products with pagination.
     */
    Page<ProductResponse> getAllProducts(Pageable pageable);

    /**
     * Get all active products.
     */
    List<ProductResponse> getActiveProducts();

    /**
     * Get products by category.
     */
    List<ProductResponse> getProductsByCategory(Long categoryId);

    /**
     * Get featured products.
     */
    List<ProductResponse> getFeaturedProducts();

    /**
     * Search products by name or description.
     */
    Page<ProductResponse> searchProducts(String searchTerm, Pageable pageable);

    /**
     * Get products with low stock.
     */
    List<ProductResponse> getLowStockProducts();

    /**
     * Get out-of-stock products.
     */
    List<ProductResponse> getOutOfStockProducts();

    /**
     * Add image to product.
     */
    ProductResponse addProductImage(Long productId, MultipartFile file, String altText, Boolean isPrimary);

    /**
     * Delete product image.
     */
    void deleteProductImage(Long productId, Long imageId);

    /**
     * Soft delete a product.
     */
    void deleteProduct(Long id);
}
