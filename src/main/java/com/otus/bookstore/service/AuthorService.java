package com.otus.bookstore.service;

import com.otus.bookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Author create(Author author);

    Optional<Author> findById(long id);

    List<Author> getAll();

    Author update(Author author);

    void deleteById(long id);
}
