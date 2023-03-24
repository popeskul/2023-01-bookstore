package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.security.InvalidParameterException;
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
            if (id == 0) {
                throw new InvalidParameterException(String.format(ERROR_GENRE_NOT_FOUND, id));
            }

            return Optional.ofNullable(entityManager.find(Genre.class, id));
        } catch (DataAccessException e) {
            throw new EntityNotFoundException(String.format(ERROR_GENRE_NOT_FOUND, id));
        }
    }

    @Override
    public List<Genre> findAll() {
        try {
            TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Genre g", Genre.class);
            List<Genre> genres = query.getResultList();
            if (genres.isEmpty()) {
                throw new EntityNotFoundException(ERROR_GENRE_NOT_FOUND_RESULT);
            }
            return genres;
        } catch (DataAccessException e) {
            throw new EntityNotFoundException(ERROR_GENRE_NOT_FOUND_RESULT + " " + e.getMessage());
        }
    }

    @Override
    public Optional<Genre> save(Genre genre) {
        try {
            if (genre.getId() == 0) {
                entityManager.persist(genre);
            } else {
                if (!entityManager.contains(genre)) {
                    genre = entityManager.merge(genre);
                }
            }

            return Optional.of(genre);
        } catch (ConstraintViolationException | DataAccessException e) {
            throw new EntitySaveException(genre, e);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            Genre genre = entityManager.find(Genre.class, id);
            if (genre != null) {
                entityManager.remove(genre);
            } else {
                throw new EntityNotFoundException(String.format(ERROR_GENRE_NOT_FOUND, id));
            }
        } catch (DataAccessException e) {
            throw new EntityNotFoundException(String.format(ERROR_GENRE_NOT_FOUND, id));
        }
    }
}
