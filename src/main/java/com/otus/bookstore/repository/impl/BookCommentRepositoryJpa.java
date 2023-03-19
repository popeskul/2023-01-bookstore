package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.BookCommentDeleteException;
import com.otus.bookstore.exception.BookCommentErrorSavedException;
import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;
import com.otus.bookstore.repository.BookCommentRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookCommentRepositoryJpa implements BookCommentRepository {
    @PersistenceContext

    private final EntityManager entityManager;

    @Autowired
    public BookCommentRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BookComment save(BookComment bookComment) throws BookCommentErrorSavedException {
        try {
            if (bookComment.getId() != null &&
                    (bookComment.getId().getBookId() != 0 && bookComment.getId().getCommentId() != 0)) {
                entityManager.persist(bookComment);
                return bookComment;
            } else {
                return entityManager.merge(bookComment);
            }
        } catch (Exception e) {
            throw new BookCommentErrorSavedException(e.getMessage());
        }
    }

    @Override
    public List<BookComment> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-comment-entity-graph");

        TypedQuery<BookComment> query = entityManager.createQuery("SELECT bc FROM BookComment bc", BookComment.class);

        query.setHint("javax.persistence.fetchgraph", entityGraph);

        return query.getResultList();
    }

    @Override
    public Optional<BookComment> findById(BookCommentId id) {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("book-comment-entity-graph");

        TypedQuery<BookComment> query = entityManager.createQuery("SELECT bc FROM BookComment bc WHERE bc.id = :id", BookComment.class);
        query.setParameter("id", id);

        query.setHint("javax.persistence.fetchgraph", entityGraph);

        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteByBookCommentId(BookCommentId id) {
        try {
            BookComment bookComment = entityManager.find(BookComment.class, id);
            if (bookComment != null) {
                entityManager.remove(bookComment);
            }
        } catch (RuntimeException e) {
            throw new BookCommentDeleteException(id.getBookId(), id.getCommentId());
        }
    }
}
