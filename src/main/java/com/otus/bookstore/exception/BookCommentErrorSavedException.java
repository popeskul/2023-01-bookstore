package com.otus.bookstore.exception;

public class BookCommentErrorSavedException extends RuntimeException {
    public static final String ERROR_SAVED = "Error saved";
    private final String errorMessage;

    public BookCommentErrorSavedException() {
        super(ERROR_SAVED);
        this.errorMessage = ERROR_SAVED;
    }

    public BookCommentErrorSavedException(String message) {
        super(message);
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
