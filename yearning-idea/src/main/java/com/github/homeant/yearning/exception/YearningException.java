package com.github.homeant.yearning.exception;

public class YearningException extends RuntimeException{

    public YearningException(String message) {
        super(message);
    }

    public YearningException(Throwable cause) {
        super(cause);
    }

    public YearningException(String message, Throwable cause) {
        super(message, cause);
    }
}
