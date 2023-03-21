package com.otus.bookstore.exception;

public class BookBadRequestException extends CoreException {
    public static final String ERROR_BAD_REQUEST = "Bad request";

    public BookBadRequestException() {
        super(ERROR_BAD_REQUEST);
    }

    public BookBadRequestException(String message) {
        super(message);
    }
}
