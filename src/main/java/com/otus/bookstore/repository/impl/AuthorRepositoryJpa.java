package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {
    public static final String ERROR_AUTHOR_NOT_FOUND = "Author with id %d not found";
    public static final String ERROR_BAD_REQUEST_ID = "Bad Author id: %d";
    public static final String ERROR_AUTHOR_NULL = "Author is null";

    @PersistenceContext
    private final EntityManager em;

    public AuthorRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Author> findById(int id) {
        if (id == 0) {
            throw new InvalidParameterException(String.format(ERROR_BAD_REQUEST_ID, id));
        }
        Author author = em.find(Author.class, id);
        if (author == null) {
            throw new EntityNotFoundException(String.format(ERROR_AUTHOR_NOT_FOUND, id));
        }
        return Optional.of(author);
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        if (authors.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return authors;
    }

    @Override
    public Optional<Author> save(Author author) {
        try {
            if (author == null) {
                throw new IllegalArgumentException(ERROR_AUTHOR_NULL);
            }
            if (author.getId() == 0) {
                em.persist(author);
            } else {
                Author existingAuthor = em.find(Author.class, author.getId());
                if (existingAuthor == null) {
                    throw new EntityNotFoundException(String.format(ERROR_AUTHOR_NOT_FOUND, author.getId()));
                }
                em.merge(author);
            }
            return Optional.of(author);
        } catch (RuntimeException e) {
            throw new EntitySaveException(author);
        }
    }

    @Override
    public Optional<Author> deleteById(int id) {
        try {
            if (id == 0) {
                throw new InvalidParameterException(String.format(ERROR_BAD_REQUEST_ID, id));
            }

            Author author = em.find(Author.class, id);
            if (author == null) {
                return Optional.empty();
            }
            em.remove(author);
            return Optional.of(author);
        } catch (RuntimeException e) {
            throw new RuntimeException(String.format(ERROR_AUTHOR_NOT_FOUND, id));
        }
    }
}
