package com.otus.bookstore.repository;

import com.otus.bookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(int id);

    List<Author> findAll();

    Optional<Author> save(Author author);

    void deleteById(int id);
}
