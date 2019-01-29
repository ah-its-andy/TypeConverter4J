package io.standardcore;

public class StandardCoreException extends RuntimeException {
    public StandardCoreException() {
    }

    public StandardCoreException(String message) {
        super(message);
    }

    public StandardCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public StandardCoreException(Throwable cause) {
        super(cause);
    }

    public StandardCoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
