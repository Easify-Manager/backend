package uz.easify.backend.exception;

/**
 * Exception thrown when insufficient inventory is available for an operation.
 */
public class InsufficientInventoryException extends BusinessException {

    public InsufficientInventoryException(String message) {
        super(message);
    }

    public InsufficientInventoryException(Long productId, Integer requested, Integer available) {
        super(String.format("Insufficient inventory for product ID %d. Requested: %d, Available: %d",
                productId, requested, available));
    }
}
