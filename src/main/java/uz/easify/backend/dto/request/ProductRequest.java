package uz.easify.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating and updating products.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    private String sku;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Compare at price must be non-negative")
    private BigDecimal compareAtPrice;

    @DecimalMin(value = "0.0", message = "Cost must be non-negative")
    private BigDecimal cost;

    private Long categoryId;

    private BigDecimal weight;

    @Size(max = 20, message = "Weight unit cannot exceed 20 characters")
    private String weightUnit;

    private Boolean active;

    private Boolean featured;

    @Size(max = 500, message = "Tags cannot exceed 500 characters")
    private String tags;

    // Inventory fields
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Low stock threshold cannot be negative")
    private Integer lowStockThreshold;

    private Boolean trackInventory;

    private Boolean allowBackorder;
}
