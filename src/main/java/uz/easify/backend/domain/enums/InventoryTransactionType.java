package uz.easify.backend.domain.enums;

/**
 * Enumeration of inventory transaction types for tracking stock movements.
 */
public enum InventoryTransactionType {
    /**
     * Initial stock setup or replenishment from supplier
     */
    PURCHASE,
    
    /**
     * Product sold to customer
     */
    SALE,
    
    /**
     * Stock returned by customer
     */
    RETURN,
    
    /**
     * Stock damaged or expired
     */
    ADJUSTMENT,
    
    /**
     * Stock reserved for pending order
     */
    RESERVATION,
    
    /**
     * Stock reservation cancelled
     */
    RELEASE,
    
    /**
     * Stock transferred between locations
     */
    TRANSFER,
    
    /**
     * Manual correction of inventory
     */
    MANUAL_CORRECTION
}
