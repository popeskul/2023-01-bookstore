package com.otus.bookstore.exception;

public class CommentNotFoundException extends CoreException {
    public static final String ERROR_NOT_FOUND = "Comment with id %d not found";

    public CommentNotFoundException() {
        super(ERROR_NOT_FOUND);
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
