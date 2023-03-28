package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.repository.BookRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {
    public static final String ERROR_FIND_ALL_BOOKS = "Books cannot be found";
    public static final String ERROR_FIND_BOOK_BY_ID = "Book cannot be found by id %s";
    public static final String ERROR_DELETE_BOOK = "Book cannot be deleted %s";

    private final EntityManager entityManager;

    public BookRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            EntityGraph<?> graph = entityManager.getEntityGraph("book-entity-graph");

            Map<String, Object> properties = new HashMap<>();

            properties.put("javax.persistence.fetchgraph", graph);

            return Optional.ofNullable(entityManager.find(Book.class, id, properties));
        } catch (Exception e) {
            throw new EntityNotFoundException(String.format(ERROR_FIND_BOOK_BY_ID, id), e);
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
            throw new EntityNotFoundException(ERROR_FIND_ALL_BOOKS, e);
        }
    }

    @Override
    public Optional<Book> save(Book book) {
        if (book.getId() == 0) {
            return create(book);
        } else {
            return update(book);
        }
    }

    private Optional<Book> create(Book book) {
        try {
            entityManager.persist(book);
            return Optional.of(book);
        } catch (Exception e) {
            throw new EntitySaveException(book, e);
        }
    }

    private Optional<Book> update(Book book) {
        try {
            return Optional.of(entityManager.merge(book));
        } catch (Exception e) {
            throw new EntitySaveException(book, e);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            Book book = entityManager.find(Book.class, id);
            if (book != null) {
                entityManager.remove(book);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(String.format(ERROR_DELETE_BOOK, id), e);
        }
    }
}
