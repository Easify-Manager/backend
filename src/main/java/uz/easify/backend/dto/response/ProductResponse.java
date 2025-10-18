package uz.easify.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for product information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private BigDecimal cost;
    private Long categoryId;
    private String categoryName;
    private InventoryResponse inventory;
    private List<ProductImageResponse> images;
    private BigDecimal weight;
    private String weightUnit;
    private Boolean active;
    private Boolean featured;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
