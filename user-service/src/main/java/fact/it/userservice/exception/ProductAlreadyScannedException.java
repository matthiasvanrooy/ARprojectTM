package fact.it.userservice.exception;

public class ProductAlreadyScannedException extends RuntimeException {
    public ProductAlreadyScannedException(String message) {
        super(message);
    }
}
