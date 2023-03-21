package com.otus.bookstore.exception;

public class CommentErrorSavedException extends CoreException {
    public static final String ERROR_SAVED = "Error saved";

    public CommentErrorSavedException() {
        super(ERROR_SAVED);
    }

    public CommentErrorSavedException(String message) {
        super(message);
    }
}
