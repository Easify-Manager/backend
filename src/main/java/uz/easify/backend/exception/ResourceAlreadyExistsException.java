package uz.easify.backend.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class ResourceAlreadyExistsException extends BusinessException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String resourceName, String identifier, Object value) {
        super(String.format("%s with %s '%s' already exists", resourceName, identifier, value));
    }
}
