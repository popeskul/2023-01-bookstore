package com.otus.bookstore.exception;

public class BookCommentNotFoundException extends Exception {
    public static final String BOOK_COMMENT_NOT_FOUND = "BookComment with bookId %d and commentId %d not found";
    private final String errorMessage;

    public BookCommentNotFoundException(long bookId, long commentId) {
        super(String.format(BOOK_COMMENT_NOT_FOUND, bookId, commentId));
        this.errorMessage = String.format(BOOK_COMMENT_NOT_FOUND, bookId, commentId);
    }

    public BookCommentNotFoundException(String message) {
        super(message);
        this.errorMessage = message;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
