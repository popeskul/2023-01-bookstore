package com.otus.bookstore.service;

import com.otus.bookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Author save(Author author);

    Optional<Author> findById(String id);

    List<Author> getAll();

    void deleteById(String id);
}
