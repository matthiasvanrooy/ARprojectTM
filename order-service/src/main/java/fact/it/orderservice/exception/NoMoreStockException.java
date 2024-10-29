package fact.it.orderservice.exception;

public class NoMoreStockException extends RuntimeException {
    public NoMoreStockException(String message) {
        super(message);
    }
}
