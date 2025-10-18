package uz.easify.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uz.easify.backend.domain.enums.OrderStatus;
import uz.easify.backend.dto.request.OrderRequest;
import uz.easify.backend.dto.response.OrderResponse;

/**
 * Service interface for managing orders.
 */
public interface OrderService {

    /**
     * Create a new order from request.
     * This will:
     * - Validate products exist and have sufficient quantity
     * - Deduct inventory for each product
     * - Create inventory transactions for each item
     * - Calculate totals
     * - Generate order number
     */
    OrderResponse createOrder(OrderRequest request);

    /**
     * Get order by ID.
     */
    OrderResponse getOrderById(Long id);

    /**
     * Get order by order number.
     */
    OrderResponse getOrderByNumber(String orderNumber);

    /**
     * Get all orders with pagination.
     */
    Page<OrderResponse> getAllOrders(Pageable pageable);

    /**
     * Get orders by status.
     */
    List<OrderResponse> getOrdersByStatus(OrderStatus status);

    /**
     * Get orders by customer email.
     */
    List<OrderResponse> getOrdersByCustomerEmail(String email);

    /**
     * Get orders by platform and user.
     */
    List<OrderResponse> getOrdersByPlatformUser(String platform, String platformUserId);

    /**
     * Get orders within date range.
     */
    List<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Update order status.
     * If status is CANCELLED, restore inventory.
     */
    OrderResponse updateOrderStatus(Long id, OrderStatus newStatus);

    /**
     * Delete order (soft delete).
     * This will also restore inventory if order was not cancelled/refunded.
     */
    void deleteOrder(Long id);

    /**
     * Get total completed orders count.
     */
    Long getCompletedOrdersCount();
}
