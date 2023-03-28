package com.otus.bookstore.service;

import com.otus.bookstore.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book create(Book book);

    Book update(Book book);

    Optional<Book> findById(long id);

    List<Book> findAll();

    void deleteById(long id);
}
