package com.otus.bookstore.exception;

public class GenreNotFoundException extends CoreException {
    public static final String ERROR_GENRE_NOT_FOUND = "Genre not found";

    public GenreNotFoundException(String message) {
        super(message);
    }

    public GenreNotFoundException() {
        super(ERROR_GENRE_NOT_FOUND);
    }
}
