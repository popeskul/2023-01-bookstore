package com.otus.bookstore.exception;

public class GenreNotFoundException extends RuntimeException {
    public static final String ERROR_GENRE_NOT_FOUND = "Genre not found";
    private final String errorMessage;

    public GenreNotFoundException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public GenreNotFoundException() {
        super(ERROR_GENRE_NOT_FOUND);
        this.errorMessage = ERROR_GENRE_NOT_FOUND;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
