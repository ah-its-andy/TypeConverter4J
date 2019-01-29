package io.standardcore;

public class InvalidCastException extends StandardCoreException {
    public InvalidCastException() {
    }

    public InvalidCastException(String message) {
        super(message);
    }

    public InvalidCastException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCastException(Throwable cause) {
        super(cause);
    }

    public InvalidCastException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
