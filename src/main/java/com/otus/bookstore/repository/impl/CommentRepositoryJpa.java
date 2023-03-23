package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.repository.CommentRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryJpa implements CommentRepository {
    public static final String ERROR_WHILE_SAVING_COMMENT = "Error while saving comment: %s";

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public CommentRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Comment save(Comment comment) {
        try {
            if (comment == null) {
                throw new IllegalArgumentException("Comment cannot be null");
            }
            entityManager.persist(comment);
            return comment;
        } catch (DataAccessException e) {
            throw new EntitySaveException(String.format(ERROR_WHILE_SAVING_COMMENT, comment), e);
        }
    }

    @Override
    public List<Comment> findAll() {
        try {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("comment-entity-graph");

            TypedQuery<Comment> query = entityManager.createQuery("SELECT c FROM Comment c", Comment.class);

            query.setHint("javax.persistence.fetchgraph", entityGraph);

            return query.getResultList();
        } catch (Exception e) {
            throw new EntitySaveException(String.format(ERROR_WHILE_SAVING_COMMENT, 0));
        }
    }

    @Override
    public Optional<Comment> findById(Long id) {
        try {
            EntityGraph<?> entityGraph = entityManager.getEntityGraph("comment-entity-graph");

            TypedQuery<Comment> query = entityManager.createQuery("SELECT c FROM Comment c WHERE c.id = :id", Comment.class);
            query.setParameter("id", id);

            query.setHint("javax.persistence.fetchgraph", entityGraph);

            return query.getResultList().stream().findFirst();
        } catch (Exception e) {
            throw new EntitySaveException(String.format(ERROR_WHILE_SAVING_COMMENT, id));
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            Comment comment = entityManager.find(Comment.class, id);
            if (comment != null) {
                entityManager.remove(comment);
            }
        } catch (DataAccessException e) {
            throw new EntitySaveException(String.format(ERROR_WHILE_SAVING_COMMENT, id), e);
        }
    }
}
