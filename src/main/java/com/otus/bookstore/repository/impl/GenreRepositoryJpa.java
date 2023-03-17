package com.otus.bookstore.repository.impl;

import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpa implements GenreRepository {
    private final EntityManager entityManager;

    public GenreRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Genre> findById(int id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = entityManager.createQuery("SELECT g FROM Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> save(Genre genre) {
        if (genre.getId() == 0) {
            entityManager.persist(genre);
            return Optional.of(genre);
        } else {
            return Optional.of(entityManager.merge(genre));
        }
    }

    @Override
    public void deleteById(int id) {
        Genre genre = entityManager.find(Genre.class, id);
        if (genre != null) {
            entityManager.remove(genre);
        }
    }

    @Override
    public void deleteAll() {
        Query query = entityManager.createQuery("DELETE FROM Genre");
        query.executeUpdate();
    }
}
