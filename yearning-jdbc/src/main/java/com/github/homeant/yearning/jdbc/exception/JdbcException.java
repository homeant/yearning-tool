package com.github.homeant.yearning.jdbc.exception;

/**
 * @author huangtianhui
 */
public class JdbcException extends RuntimeException {
    public JdbcException(String message) {
        super(message);
    }

    public JdbcException(Throwable cause) {
        super(cause);
    }

    public JdbcException(String message, Throwable cause) {
        super(message, cause);
    }
}
