package com.otus.bookstore.dao;

import com.otus.bookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    Optional<Integer> insert(Author author);
    void update(Author author);
    void deleteById(int id);
    Optional<Author> getById(int id);
    List<Author> getAll();
    void deleteAll();
}
