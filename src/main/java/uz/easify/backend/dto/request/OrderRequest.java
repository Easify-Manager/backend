package uz.easify.backend.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating orders.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 100)
    private String customerName;

    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String customerEmail;

    @Size(max = 20)
    private String customerPhone;

    @Size(max = 255)
    private String shippingAddress;

    @Valid
    @NotEmpty(message = "Order must have at least one item")
    @Builder.Default
    private List<OrderItemRequest> items = new ArrayList<>();

    @DecimalMin(value = "0.00", message = "Tax amount must be non-negative")
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Shipping cost must be non-negative")
    @Builder.Default
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Discount amount must be non-negative")
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Size(max = 50)
    private String platform;

    @Size(max = 100)
    private String platformUserId;

    @Size(max = 1000)
    private String notes;

    /**
     * Request DTO for order items within an order.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}
