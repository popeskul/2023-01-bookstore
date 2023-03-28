package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryJpa implements CommentRepository {
    public static final String ERROR_GET_COMMENT_BY_ID = "Error while getting comment by id: %d";
    public static final String ERROR_GET_ALL_COMMENTS = "Error while getting all comments";
    public static final String ERROR_CREATE_COMMENT = "Error while creating comment: %s";
    public static final String ERROR_DELETE_COMMENT = "Error while deleting comment by id: %d";

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public CommentRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            return create(comment).orElseThrow();
        } else {
            return update(comment).orElseThrow();
        }
    }

    private Optional<Comment> create(Comment comment) {
        try {
            entityManager.persist(comment);
            return Optional.of(comment);
        } catch (Exception e) {
            throw new EntitySaveException(comment, e);
        }
    }

    private Optional<Comment> update(Comment comment) {
        try {
            return Optional.of(entityManager.merge(comment));
        } catch (Exception e) {
            throw new EntitySaveException(comment, e);
        }
    }

    @Override
    public List<Comment> findAll() {
        try {
            TypedQuery<Comment> query = entityManager.createQuery("SELECT c FROM Comment c", Comment.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(ERROR_GET_ALL_COMMENTS, e);
        }
    }

    @Override
    public Optional<Comment> findById(Long id) {
        try {
            TypedQuery<Comment> query = entityManager.createQuery("SELECT c FROM Comment c WHERE c.id = :id", Comment.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (RuntimeException e) {
            throw new RuntimeException(String.format(ERROR_GET_COMMENT_BY_ID, id), e);
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
            throw new RuntimeException(String.format(ERROR_DELETE_COMMENT, id), e);
        }
    }
}
