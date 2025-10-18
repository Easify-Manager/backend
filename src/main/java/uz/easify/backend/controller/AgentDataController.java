package uz.easify.backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.easify.backend.dto.response.ApiResponse;
import uz.easify.backend.dto.response.InventoryResponse;
import uz.easify.backend.dto.response.ProductResponse;
import uz.easify.backend.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller specifically designed for AI agent data access.
 * Provides optimized endpoints for the AI agent to query product and inventory data.
 * These endpoints are designed to be simple and efficient for AI consumption.
 */
@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
@Slf4j
public class AgentDataController {

    private final ProductService productService;

    /**
     * Get all available products (active only) for AI agent.
     * This is the primary endpoint the AI agent should use to get product catalog.
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAvailableProducts() {
        log.info("AI Agent request: Get all available products");
        List<ProductResponse> products = productService.getActiveProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get product details by ID for AI agent.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetails(@PathVariable Long id) {
        log.info("AI Agent request: Get product details for ID: {}", id);
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * Get product details by SKU for AI agent.
     */
    @GetMapping("/products/sku/{sku}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetailsBySku(@PathVariable String sku) {
        log.info("AI Agent request: Get product details for SKU: {}", sku);
        ProductResponse product = productService.getProductBySku(sku);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * Search products for AI agent.
     * Use this when the AI needs to find products based on customer queries.
     */
    @GetMapping("/products/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @RequestParam String query,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("AI Agent request: Search products with query: {} (minPrice={}, maxPrice={})", query, minPrice, maxPrice);
        Page<ProductResponse> page = productService.searchProducts(query, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(ApiResponse.success(page.getContent()));
    }

    /**
     * Get products by category for AI agent.
     */
    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId) {
        log.info("AI Agent request: Get products by category ID: {}", categoryId);
        List<ProductResponse> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Get featured/recommended products for AI agent.
     */
    @GetMapping("/products/featured")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts() {
        log.info("AI Agent request: Get featured products");
        List<ProductResponse> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Check product availability (inventory) for AI agent.
     * Use this to inform customers about stock availability.
     */
    @GetMapping("/inventory/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> checkInventory(@PathVariable Long productId) {
        log.info("AI Agent request: Check inventory for product ID: {}", productId);
        ProductResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.success(product.getInventory()));
    }

    /**
     * Get low stock products for AI agent.
     * Useful for proactive customer communication.
     */
    @GetMapping("/inventory/low-stock")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStockProducts() {
        log.info("AI Agent request: Get low stock products");
        List<ProductResponse> products = productService.getLowStockProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Health check endpoint for AI agent.
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("AI Agent API is running"));
    }
}
