package uz.easify.backend.service.mapper;

import org.springframework.stereotype.Component;
import uz.easify.backend.domain.entity.Order;
import uz.easify.backend.domain.entity.OrderItem;
import uz.easify.backend.dto.response.OrderItemResponse;
import uz.easify.backend.dto.response.OrderResponse;

import java.util.stream.Collectors;

/**
 * Mapper for converting Order entities to DTOs.
 */
@Component
public class OrderMapper {

    /**
     * Convert Order entity to OrderResponse DTO.
     */
    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .customerPhone(order.getCustomerPhone())
                .shippingAddress(order.getCustomerAddress())
                .status(order.getStatus())
                .items(order.getItems().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList()))
                .subtotal(order.getSubtotal())
                .taxAmount(order.getTax())
                .shippingCost(order.getShipping())
                .discountAmount(order.getDiscount())
                .totalAmount(order.getTotal())
                .platform(order.getPlatform())
                .platformUserId(order.getPlatformUserId())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    /**
     * Convert OrderItem entity to OrderItemResponse DTO.
     */
    public OrderItemResponse toItemResponse(OrderItem item) {
        if (item == null) {
            return null;
        }

        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .productName(item.getProductName())
                .productSku(item.getProductSku())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .productImageUrl(item.getProductImageUrl())
                .build();
    }
}
