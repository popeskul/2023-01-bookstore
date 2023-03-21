package com.otus.bookstore.exception;

public class BookCommentDeleteException extends CoreException {
    private static final String ERROR_MESSAGE = "Error delete book comment with author id: %d and book id: %d";

    public BookCommentDeleteException(long authorId, long bookId) {
        super(String.format(ERROR_MESSAGE, authorId, bookId));
    }
}
