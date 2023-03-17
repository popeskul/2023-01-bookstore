package com.otus.bookstore.repository;

import com.otus.bookstore.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(int id);

    List<Book> findAll();

    Optional<Book> save(Book book);

    void deleteById(int id);

    void deleteAll();
}
