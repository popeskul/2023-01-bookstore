package com.otus.bookstore.exception;

public class BookBadRequestException extends RuntimeException {
    public static final String ERROR_BAD_REQUEST = "Bad request";
    private final String errorMessage;

    public BookBadRequestException() {
        super(ERROR_BAD_REQUEST);
        this.errorMessage = ERROR_BAD_REQUEST;
    }

    public BookBadRequestException(String message) {
        super(message);
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
