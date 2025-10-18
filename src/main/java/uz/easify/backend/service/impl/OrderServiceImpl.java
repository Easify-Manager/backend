package uz.easify.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.easify.backend.domain.entity.*;
import uz.easify.backend.domain.enums.InventoryTransactionType;
import uz.easify.backend.domain.enums.OrderStatus;
import uz.easify.backend.dto.request.OrderRequest;
import uz.easify.backend.dto.response.OrderResponse;
import uz.easify.backend.exception.InsufficientStockException;
import uz.easify.backend.exception.ResourceNotFoundException;
import uz.easify.backend.repository.*;
import uz.easify.backend.service.OrderService;
import uz.easify.backend.service.mapper.OrderMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService for managing orders.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating new order for customer: {}", request.getCustomerName());

        // Create order entity
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .customerPhone(request.getCustomerPhone())
                .customerAddress(request.getShippingAddress())
                .status(OrderStatus.PENDING)
                .tax(request.getTaxAmount())
                .shipping(request.getShippingCost())
                .discount(request.getDiscountAmount())
                .platform(request.getPlatform())
                .platformUserId(request.getPlatformUserId())
                .notes(request.getNotes())
                .build();

        // Process each order item
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findByIdAndDeletedFalse(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with id: " + itemRequest.getProductId()));

            // Check inventory
            Inventory inventory = inventoryRepository.findByProductIdAndDeletedFalse(product.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product: " + product.getName()));

            int requestedQuantity = itemRequest.getQuantity();
            if (inventory.getQuantity() < requestedQuantity) {
                throw new InsufficientStockException(
                        String.format("Insufficient stock for product '%s'. Available: %d, Requested: %d",
                                product.getName(), inventory.getQuantity(), requestedQuantity));
            }

            // Get primary image URL if available
            String imageUrl = product.getImages().stream()
                    .filter(ProductImage::getIsPrimary)
                    .findFirst()
                    .map(ProductImage::getFileUrl)
                    .orElse(product.getImages().isEmpty() ? null : 
                            product.getImages().get(0).getFileUrl());

            // Create order item
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getName())
                    .productSku(product.getSku())
                    .quantity(requestedQuantity)
                    .unitPrice(product.getPrice())
                    .productImageUrl(imageUrl)
                    .build();
            orderItem.calculateLineTotal();

            order.addItem(orderItem);

            // Deduct inventory
            int newQuantity = inventory.getQuantity() - requestedQuantity;
            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);

            log.info("Deducted {} units of product '{}'. New quantity: {}", 
                    requestedQuantity, product.getName(), newQuantity);

            // Create inventory transaction
            InventoryTransaction transaction = InventoryTransaction.builder()
                    .product(product)
                    .transactionType(InventoryTransactionType.SALE)
                    .quantityChange(-requestedQuantity) // Negative for outbound
                    .quantityAfter(newQuantity)
                    .notes("Sale - Order: " + order.getOrderNumber())
                    .build();
            inventoryTransactionRepository.save(transaction);
        }

        // Calculate totals
        order.calculateTotal();

        // Save order
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with order number: {}", savedOrder.getOrderNumber());

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumberAndDeletedFalse(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with order number: " + orderNumber));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findByDeletedFalseOrderByCreatedAtDesc(pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusAndDeletedFalseOrderByCreatedAtDesc(status)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmailAndDeletedFalseOrderByCreatedAtDesc(email)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByPlatformUser(String platform, String platformUserId) {
        return orderRepository.findByPlatformAndPlatformUserIdAndDeletedFalseOrderByCreatedAtDesc(
                        platform, platformUserId)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateRange(startDate, endDate)
                .stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);

        // If order is cancelled or refunded, restore inventory
        if ((newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.REFUNDED) &&
                oldStatus != OrderStatus.CANCELLED && oldStatus != OrderStatus.REFUNDED) {
            restoreInventory(order);
        }

        Order updatedOrder = orderRepository.save(order);
        log.info("Order {} status updated from {} to {}", 
                order.getOrderNumber(), oldStatus, newStatus);

        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        // Restore inventory if order wasn't cancelled/refunded
        if (order.getStatus() != OrderStatus.CANCELLED && 
            order.getStatus() != OrderStatus.REFUNDED) {
            restoreInventory(order);
        }

        order.setDeleted(true);
        orderRepository.save(order);
        log.info("Order {} deleted", order.getOrderNumber());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCompletedOrdersCount() {
        return orderRepository.countCompletedOrders();
    }

    /**
     * Restore inventory for all items in an order.
     */
    private void restoreInventory(Order order) {
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.findByProductIdAndDeletedFalse(
                            item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found for product: " + item.getProductName()));

            int restoredQuantity = item.getQuantity();
            int newQuantity = inventory.getQuantity() + restoredQuantity;
            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);

            log.info("Restored {} units of product '{}'. New quantity: {}",
                    restoredQuantity, item.getProductName(), newQuantity);

            // Create inventory transaction for restoration
            InventoryTransaction transaction = InventoryTransaction.builder()
                    .product(item.getProduct())
                    .transactionType(InventoryTransactionType.ADJUSTMENT)
                    .quantityChange(restoredQuantity) // Positive for inbound
                    .quantityAfter(newQuantity)
                    .notes("Restored from " + order.getStatus() + " order: " + order.getOrderNumber())
                    .build();
            inventoryTransactionRepository.save(transaction);
        }
    }

    /**
     * Generate unique order number.
     * Format: ORD-YYYYMMDD-XXXXXX (where X is random digits)
     */
    private String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%06d", new Random().nextInt(1000000));
        String orderNumber = "ORD-" + datePart + "-" + randomPart;

        // Ensure uniqueness
        while (orderRepository.existsByOrderNumberAndDeletedFalse(orderNumber)) {
            randomPart = String.format("%06d", new Random().nextInt(1000000));
            orderNumber = "ORD-" + datePart + "-" + randomPart;
        }

        return orderNumber;
    }
}
