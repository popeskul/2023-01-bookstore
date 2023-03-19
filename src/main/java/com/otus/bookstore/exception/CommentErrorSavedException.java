package com.otus.bookstore.exception;

public class CommentErrorSavedException extends RuntimeException {
    public static final String ERROR_SAVED = "Error saved";

    private final String errorMessage;

    public CommentErrorSavedException() {
        super(ERROR_SAVED);
        this.errorMessage = ERROR_SAVED;
    }

    public CommentErrorSavedException(String message) {
        super(message);
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
