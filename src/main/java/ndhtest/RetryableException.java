package ndhtest;

public class RetryableException extends RuntimeException {

    public RetryableException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RetryableException(String message) {
        this(message, null);
    }
}
