package com.otus.bookstore.service;

import com.otus.bookstore.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Optional<Integer> create(Genre genre);

    void update(Genre genre);

    void deleteById(int id);

    Optional<Genre> getById(int id);

    List<Genre> getAll();
}
