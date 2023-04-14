package com.otus.bookstore.service;

import com.otus.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CliBookService {
    List<Book> getAll();

    Optional<Book> getById(String id);

    Book create(String title, String description, BigDecimal price, String authorId, String genreId);

    Book update(String id, String title, String description, BigDecimal price, String authorId, String genreId);

    void deleteById(String id);
}
