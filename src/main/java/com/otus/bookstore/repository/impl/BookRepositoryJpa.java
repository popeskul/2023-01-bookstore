package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.repository.BookRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {
    public static final String ERROR_SAVE_BOOK = "Book cannot be saved %s";

    private final EntityManager entityManager;

    public BookRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-entity-graph");

            TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b WHERE b.id = :id", Book.class);
            query.setParameter("id", id);

            query.setHint("javax.persistence.fetchgraph", entityGraph);

            return query.getResultList().stream().findFirst();
        } catch (Exception e) {
            throw new EntitySaveException(String.format(ERROR_SAVE_BOOK, id));
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-entity-graph");

            TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);

            query.setHint("javax.persistence.fetchgraph", entityGraph);

            return query.getResultList();
        } catch (Exception e) {
            throw new EntitySaveException(String.format(ERROR_SAVE_BOOK, 0));
        }
    }

    @Override
    public Optional<Book> save(Book book) {
        try {
            if (book.getId() == 0) {
                entityManager.persist(book);
                return Optional.of(book);
            } else {
                return Optional.of(entityManager.merge(book));
            }
        } catch (Exception e) {
            throw new EntitySaveException(book);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            if (id <= 0) {
                throw new InvalidParameterException(String.format(ERROR_SAVE_BOOK, id));
            }

            Book book = entityManager.find(Book.class, id);
            if (book != null) {
                entityManager.remove(book);
            }
        } catch (DataAccessException e) {
            throw new EntitySaveException(String.format(ERROR_SAVE_BOOK, id));
        }
    }
}
