package com.otus.bookstore.repository;

import com.otus.bookstore.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(int id);

    List<Genre> findAll();

    Optional<Genre> save(Genre author);

    void deleteById(int id);

    void deleteAll();
}
