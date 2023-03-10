package com.otus.bookstore.dao;

import com.otus.bookstore.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Optional<Integer> insert(Book book);

    void update(Book book);

    void deleteById(int id);

    Optional<Book> getById(int id);

    List<Book> getAll();

    void deleteAll();
}
