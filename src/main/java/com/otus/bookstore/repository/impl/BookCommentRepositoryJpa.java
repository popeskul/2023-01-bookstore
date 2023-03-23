package com.otus.bookstore.repository.impl;

import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;
import com.otus.bookstore.repository.BookCommentRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Repository
public class BookCommentRepositoryJpa implements BookCommentRepository {
    public static final String ERROR_NOT_FOUND = "Unable to find book comment with id: %s";
    public static final String ERROR_INVALID_BOOK_COMMENT = "Invalid Book Comment: %s";

    @PersistenceContext

    private final EntityManager entityManager;

    @Autowired
    public BookCommentRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BookComment save(BookComment bookComment) {
        try {
            if (bookComment.getId() == null ||
                    bookComment.getId().getBookId() == 0 ||
                    bookComment.getId().getCommentId() == 0) {
                throw new InvalidParameterException(String.format(ERROR_INVALID_BOOK_COMMENT, bookComment));
            }

            entityManager.persist(bookComment);
            return bookComment;
        } catch (RuntimeException e) {
            throw new EntityNotFoundException(String.format(ERROR_NOT_FOUND, e.getMessage()), e);
        }
    }

    @Override
    public List<BookComment> findAll() {
        try {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-comment-entity-graph");

            TypedQuery<BookComment> query = entityManager.createQuery("SELECT bc FROM BookComment bc", BookComment.class);

            query.setHint("javax.persistence.fetchgraph", entityGraph);

            return query.getResultList();
        } catch (RuntimeException e) {
            throw new EntityNotFoundException(String.format(ERROR_NOT_FOUND, e.getMessage()));
        }
    }

    @Override
    public Optional<BookComment> findById(BookCommentId id) {
        if (id == null) {
            return Optional.empty();
        }

        try {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-comment-entity-graph");

            TypedQuery<BookComment> query = entityManager.createQuery(
                            "SELECT bc FROM BookComment bc WHERE bc.id = :id", BookComment.class)
                    .setParameter("id", id)
                    .setHint("javax.persistence.fetchgraph", entityGraph);

            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (RuntimeException e) {
            throw new EntityNotFoundException(String.format(ERROR_NOT_FOUND, e.getMessage()), e);
        }
    }

    @Override
    public boolean deleteById(BookCommentId id) {
        try {
            if (id == null) {
                return false;
            }

            BookComment bookComment = entityManager.find(BookComment.class, id);
            if (bookComment == null) {
                return false;
            }
            entityManager.remove(bookComment);
            return true;
        } catch (RuntimeException e) {
            throw new EntityNotFoundException(String.format(ERROR_NOT_FOUND, e.getMessage()), e);
        }
    }
}
