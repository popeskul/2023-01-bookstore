package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.BookErrorSavedException;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.repository.BookRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpa implements BookRepository {
    private final EntityManager entityManager;

    public BookRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Book> findById(int id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-entity-graph");

        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);

        query.setHint("javax.persistence.fetchgraph", entityGraph);

        return query.getResultList();
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
            throw new BookErrorSavedException();
        }
    }

    @Override
    public void deleteById(int id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        }
    }

    @Override
    public void deleteAll() {
        Query query = entityManager.createQuery("DELETE FROM Book");
        query.executeUpdate();
    }
}
