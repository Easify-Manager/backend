package uz.easify.backend.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s with ID %d not found", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String identifier, Object value) {
        super(String.format("%s with %s '%s' not found", resourceName, identifier, value));
    }
}
