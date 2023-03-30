package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpa implements GenreRepository {
    public static final String ERROR_GENRE_NOT_FOUND = "Genre with id %d not found";
    public static final String ERROR_GENRE_NOT_FOUND_RESULT = "No genres found";

    private final EntityManager entityManager;

    public GenreRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            return Optional.ofNullable(entityManager.find(Genre.class, id));
        } catch (DataAccessException e) {
            throw new EntityNotFoundException(String.format(ERROR_GENRE_NOT_FOUND, id));
        }
    }

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Genre g", Genre.class);
        List<Genre> genres = query.getResultList();
        if (genres.isEmpty()) {
            throw new EntityNotFoundException(ERROR_GENRE_NOT_FOUND_RESULT);
        }
        return genres;
    }

    @Override
    public Optional<Genre> save(Genre genre) {
        if (genre.getId() == 0) {
            return create(genre);
        } else {
            return update(genre);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            Genre genre = entityManager.find(Genre.class, id);
            if (genre != null) {
                entityManager.remove(genre);
            }
        } catch (DataAccessException e) {
            throw new EntityNotFoundException(String.format(ERROR_GENRE_NOT_FOUND, id));
        }
    }

    private Optional<Genre> create(Genre genre) {
        try {
            entityManager.persist(genre);
            return Optional.of(genre);
        } catch (Exception e) {
            throw new EntitySaveException(genre, e);
        }
    }

    private Optional<Genre> update(Genre genre) {
        try {
            return Optional.of(entityManager.merge(genre));
        } catch (Exception e) {
            throw new EntitySaveException(genre, e);
        }
    }
}
