package uz.easify.backend.domain.enums;

/**
 * Enumeration of order/sale statuses.
 */
public enum OrderStatus {
    /**
     * Order is pending confirmation
     */
    PENDING,
    
    /**
     * Order confirmed and being processed
     */
    CONFIRMED,
    
    /**
     * Order is being prepared for shipping
     */
    PROCESSING,
    
    /**
     * Order has been shipped
     */
    SHIPPED,
    
    /**
     * Order delivered successfully
     */
    DELIVERED,
    
    /**
     * Order cancelled by customer or admin
     */
    CANCELLED,
    
    /**
     * Order refunded
     */
    REFUNDED
}
