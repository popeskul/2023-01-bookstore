package com.otus.bookstore.service;

import com.otus.bookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Optional<Integer> create(Author author);

    Optional<Author> getById(int id);

    List<Author> getAll();

    void update(Author author);

    void deleteById(int id);
}
