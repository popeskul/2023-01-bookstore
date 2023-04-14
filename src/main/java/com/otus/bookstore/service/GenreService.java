package com.otus.bookstore.service;

import com.otus.bookstore.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Genre save(Genre genre);

    void deleteById(String id);

    Optional<Genre> getById(String id);

    List<Genre> getAll();
}
