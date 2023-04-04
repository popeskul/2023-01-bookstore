package com.otus.bookstore.repository;

import com.otus.bookstore.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(long id);

    List<Book> findAll();

    Optional<Book> save(Book book);

    void deleteById(long id);
}
