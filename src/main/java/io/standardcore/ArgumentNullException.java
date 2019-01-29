package io.standardcore;

public class ArgumentNullException extends ArgumentException {
    public ArgumentNullException() {
    }

    public ArgumentNullException(String message) {
        super(message);
    }

    public ArgumentNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentNullException(Throwable cause) {
        super(cause);
    }

    public ArgumentNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
