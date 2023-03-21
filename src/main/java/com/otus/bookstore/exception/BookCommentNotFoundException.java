package com.otus.bookstore.exception;

public class BookCommentNotFoundException extends CoreException {
    public static final String BOOK_COMMENT_NOT_FOUND = "BookComment with bookId %d and commentId %d not found";

    public BookCommentNotFoundException(long bookId, long commentId) {
        super(String.format(BOOK_COMMENT_NOT_FOUND, bookId, commentId));
    }

    public BookCommentNotFoundException(String message) {
        super(message);
    }
}
