package com.otus.bookstore.exception;

public class BookCommentErrorSavedException extends CoreException {
    public static final String ERROR_SAVED = "Error saved";

    public BookCommentErrorSavedException() {
        super(ERROR_SAVED);
    }

    public BookCommentErrorSavedException(String message) {
        super(message);
    }
}
