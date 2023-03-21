package com.otus.bookstore.exception;

public class AuthorBadRequestException extends CoreException {
    public static final String ERROR_BAD_REQUEST = "Bad request";

    public AuthorBadRequestException() {
        super(ERROR_BAD_REQUEST);
    }

    public AuthorBadRequestException(String message) {
        super(message);
    }
}
