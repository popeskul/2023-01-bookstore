package com.otus.bookstore.exception;

public class AuthorNotFoundException extends CoreException {
    public static final String ERROR_AUTHOR_NOT_FOUND = "Author not found";

    public AuthorNotFoundException() {
        super(ERROR_AUTHOR_NOT_FOUND);
    }

    public AuthorNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
