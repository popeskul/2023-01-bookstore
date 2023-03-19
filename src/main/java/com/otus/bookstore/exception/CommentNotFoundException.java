package com.otus.bookstore.exception;

public class CommentNotFoundException extends RuntimeException {
    public static final String ERROR_NOT_FOUND = "Comment with id %d not found";
    private final String errorMessage;

    public CommentNotFoundException() {
        super(ERROR_NOT_FOUND);
        this.errorMessage = ERROR_NOT_FOUND;
    }

    public CommentNotFoundException(String message) {
        super(message);
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
