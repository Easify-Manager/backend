package uz.easify.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.easify.backend.domain.enums.OrderStatus;
import uz.easify.backend.dto.request.OrderRequest;
import uz.easify.backend.dto.response.ApiResponse;
import uz.easify.backend.dto.response.OrderResponse;
import uz.easify.backend.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing orders.
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Create a new order (record a sale).
     * POST /api/orders
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest request) {
        log.info("Creating order for customer: {}", request.getCustomerName());
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", response));
    }

    /**
     * Get order by ID.
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get order by order number.
     * GET /api/orders/number/{orderNumber}
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByNumber(
            @PathVariable String orderNumber) {
        OrderResponse response = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all orders with pagination.
     * GET /api/orders?page=0&size=20&sort=createdAt,desc
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(Pageable pageable) {
        Page<OrderResponse> response = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get orders by status.
     * GET /api/orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(
            @PathVariable OrderStatus status) {
        List<OrderResponse> response = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get orders by customer email.
     * GET /api/orders/customer?email=customer@example.com
     */
    @GetMapping("/customer")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByCustomer(
            @RequestParam String email) {
        List<OrderResponse> response = orderService.getOrdersByCustomerEmail(email);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get orders by platform and user.
     * GET /api/orders/platform?platform=telegram&userId=123456
     */
    @GetMapping("/platform")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByPlatform(
            @RequestParam String platform,
            @RequestParam String userId) {
        List<OrderResponse> response = orderService.getOrdersByPlatformUser(platform, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get orders by date range.
     * GET /api/orders/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59
     */
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<OrderResponse> response = orderService.getOrdersByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Update order status.
     * PUT /api/orders/{id}/status?status=CONFIRMED
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        log.info("Updating order {} status to {}", id, status);
        OrderResponse response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", response));
    }

    /**
     * Delete order (soft delete).
     * DELETE /api/orders/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully", null));
    }

    /**
     * Get completed orders count (statistics).
     * GET /api/orders/stats/completed-count
     */
    @GetMapping("/stats/completed-count")
    public ResponseEntity<ApiResponse<Long>> getCompletedOrdersCount() {
        Long count = orderService.getCompletedOrdersCount();
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}
