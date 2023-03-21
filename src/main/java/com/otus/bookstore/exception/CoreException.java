package com.otus.bookstore.exception;

public class CoreException extends RuntimeException {
    private final String errorMessage;

    public CoreException(String message) {
        super(message);
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
