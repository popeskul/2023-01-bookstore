package com.otus.bookstore.repository;

import com.otus.bookstore.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(long id);

    List<Genre> findAll();

    Optional<Genre> save(Genre author);

    void deleteById(long id);
}
