package com.otus.bookstore.repository.impl;

import com.otus.bookstore.exception.CommentErrorSavedException;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.repository.CommentRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryJpa implements CommentRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public CommentRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Comment save(Comment comment) throws CommentErrorSavedException {
        try {
            entityManager.persist(comment);
            return comment;
        } catch (Exception e) {
            throw new CommentErrorSavedException();
        }
    }

    @Override
    public List<Comment> findAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("comment-entity-graph");

        TypedQuery<Comment> query = entityManager.createQuery("SELECT c FROM Comment c", Comment.class);

        query.setHint("javax.persistence.fetchgraph", entityGraph);

        return query.getResultList();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = entityManager.find(Comment.class, id);
        if (comment != null) {
            entityManager.remove(comment);
        }
    }
}
