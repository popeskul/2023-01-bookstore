package com.otus.bookstore.exception;

public class BookErrorSavedException extends CoreException {
    public static final String ERROR_CREATE_BOOK = "Book cannot be saved";
    public static final String ERROR_UPDATE_BOOK = "Book cannot be updated";

    public BookErrorSavedException(String message) {
        super(message);
    }

    public BookErrorSavedException() {
        super(ERROR_CREATE_BOOK);
    }
}
