package uz.easify.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.easify.backend.dto.request.ProductRequest;
import uz.easify.backend.dto.response.ApiResponse;
import uz.easify.backend.dto.response.ProductResponse;
import uz.easify.backend.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for product management operations.
 * Provides endpoints for CRUD operations and image management.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Create a new product.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        log.info("REST request to create product: {}", request.getName());
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", response));
    }

    /**
     * Update an existing product.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        log.info("REST request to update product ID: {}", id);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", response));
    }

    /**
     * Get a product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        log.info("REST request to get product ID: {}", id);
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get a product by SKU.
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySku(@PathVariable String sku) {
        log.info("REST request to get product by SKU: {}", sku);
        ProductResponse response = productService.getProductBySku(sku);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all products with pagination.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("REST request to get all products with pagination");
        Page<ProductResponse> response = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all active products (for AI agent).
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        log.info("REST request to get all active products");
        List<ProductResponse> response = productService.getActiveProducts();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get products by category.
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId) {
        log.info("REST request to get products by category ID: {}", categoryId);
        List<ProductResponse> response = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get featured products.
     */
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {
        log.info("REST request to get featured products");
        List<ProductResponse> response = productService.getFeaturedProducts();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Search products by name or description.
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam String query,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to search products with query: {} (minPrice={}, maxPrice={})", query, minPrice, maxPrice);
        Page<ProductResponse> response = productService.searchProducts(query, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get products with low stock.
     */
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStockProducts() {
        log.info("REST request to get low stock products");
        List<ProductResponse> response = productService.getLowStockProducts();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get out-of-stock products.
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getOutOfStockProducts() {
        log.info("REST request to get out of stock products");
        List<ProductResponse> response = productService.getOutOfStockProducts();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Add image to product.
     */
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> addProductImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String altText,
            @RequestParam(required = false, defaultValue = "false") Boolean isPrimary) {
        log.info("REST request to add image to product ID: {}", id);
        ProductResponse response = productService.addProductImage(id, file, altText, isPrimary);
        return ResponseEntity.ok(ApiResponse.success("Image added successfully", response));
    }

    /**
     * Delete product image.
     */
    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        log.info("REST request to delete image ID: {} from product ID: {}", imageId, productId);
        productService.deleteProductImage(productId, imageId);
        return ResponseEntity.ok(ApiResponse.success("Image deleted successfully", null));
    }

    /**
     * Delete a product (soft delete).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}
