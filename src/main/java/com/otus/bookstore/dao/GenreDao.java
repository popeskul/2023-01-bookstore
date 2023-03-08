package com.otus.bookstore.dao;

import com.otus.bookstore.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Integer> insert(Genre genre);

    void update(Genre genre);

    void deleteById(int id);

    Optional<Genre> getById(int id);

    List<Genre> getAll();

    void deleteAll();
}
