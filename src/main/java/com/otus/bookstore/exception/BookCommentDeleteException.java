package com.otus.bookstore.exception;

public class BookCommentDeleteException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Error delete book comment with author id: %d and book id: %d";
    private final String errorMessage;

    public BookCommentDeleteException(long authorId, long bookId) {
        this.errorMessage = String.format(ERROR_MESSAGE, authorId, bookId);
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
