package ndhtest;

public class SpecializedRetryableException extends RetryableException {

    public SpecializedRetryableException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public SpecializedRetryableException(String message) {
        this(message, null);
    }
}
