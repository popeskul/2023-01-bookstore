package com.otus.bookstore.exception;

public class BookErrorSavedException extends RuntimeException {
    public static final String ERROR_CREATE_BOOK = "Book cannot be saved";
    public static final String ERROR_UPDATE_BOOK = "Book cannot be updated";
    private final String errorMessage;

    public BookErrorSavedException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public BookErrorSavedException() {
        super(ERROR_CREATE_BOOK);
        this.errorMessage = ERROR_CREATE_BOOK;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
