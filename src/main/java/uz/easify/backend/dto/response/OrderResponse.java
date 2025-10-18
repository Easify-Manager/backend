package uz.easify.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.easify.backend.domain.enums.OrderStatus;

/**
 * Response DTO for Order entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private OrderStatus status;
    @Builder.Default
    private List<OrderItemResponse> items = new ArrayList<>();
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingCost;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String platform;
    private String platformUserId;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
