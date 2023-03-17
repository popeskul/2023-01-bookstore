package com.otus.bookstore.exception;

public class AuthorNotFoundException extends RuntimeException {
    public static final String ERROR_AUTHOR_NOT_FOUND = "Author not found";
    private final String errorMessage;

    public AuthorNotFoundException() {
        super(ERROR_AUTHOR_NOT_FOUND);
        this.errorMessage = ERROR_AUTHOR_NOT_FOUND;
    }

    public AuthorNotFoundException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
