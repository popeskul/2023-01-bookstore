package com.otus.bookstore.exception;

public class AuthorBadRequestException extends RuntimeException {
    public static final String ERROR_BAD_REQUEST = "Bad request";
    private final String errorMessage;

    public AuthorBadRequestException() {
        super(ERROR_BAD_REQUEST);
        this.errorMessage = ERROR_BAD_REQUEST;
    }

    public AuthorBadRequestException(String message) {
        super(message);
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
