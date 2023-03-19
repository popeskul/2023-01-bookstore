package com.otus.bookstore.service;

import com.otus.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAll();

    Optional<Book> getById(long id);

    Optional<Long> create(String title, String description, BigDecimal price, int authorId, int genreId);

    void update(long id, String title, String description, BigDecimal price, int authorId, int genreId);

    void deleteById(long id);
}
