package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {
    public static final String ERROR_AUTHOR_NOT_FOUND = "Author with id %d not found";
    public static final String ERROR_DELETE_AUTHOR = "Error delete Author with id: %d";

    @PersistenceContext
    private final EntityManager entityManager;

    public AuthorRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Author> findById(long id) {
        Author author = entityManager.find(Author.class, id);
        return Optional.ofNullable(author);
    }

    @Override
    public List<Author> findAll() {
        return entityManager
                .createQuery("SELECT a FROM Author a", Author.class)
                .getResultList();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            return create(author);
        } else {
            return update(author);
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            Author author = entityManager.find(Author.class, id);
            if (author != null) {
                entityManager.remove(author);
                return true;
            }

            return false;
        } catch (DataAccessException e) {
            throw new RuntimeException(String.format(ERROR_DELETE_AUTHOR, id), e);
        }
    }

    private Author create(Author author) {
        try {
            entityManager.persist(author);
            return author;
        } catch (Exception e) {
            throw new EntitySaveException(author, e);
        }
    }

    private Author update(Author author) {
        try {
            return entityManager.merge(author);
        } catch (Exception e) {
            throw new EntitySaveException(author, e);
        }
    }
}
