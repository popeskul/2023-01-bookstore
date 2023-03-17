package com.otus.bookstore.service;

import com.otus.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAll();

    Optional<Book> getById(int id);

    Optional<Integer> create(String title, String description, BigDecimal price, int authorId, int genreId);

    void update(int id, String title, String description, BigDecimal price, int authorId, int genreId);

    void deleteById(int id);
}
