package com.otus.bookstore.service;

import com.otus.bookstore.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Genre save(Genre genre);

    void deleteById(long id);

    Optional<Genre> getById(long id);

    List<Genre> getAll();
}
