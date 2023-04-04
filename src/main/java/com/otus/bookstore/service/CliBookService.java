package com.otus.bookstore.service;

import com.otus.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CliBookService {
    List<Book> getAll();

    Optional<Book> getById(long id);

    Book create(String title, String description, BigDecimal price, long authorId, long genreId);

    Book update(long id, String title, String description, BigDecimal price, long authorId, long genreId);

    void deleteById(long id);
}
