package com.otus.bookstore.repository;

import com.otus.bookstore.exception.CommentErrorSavedException;
import com.otus.bookstore.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment) throws CommentErrorSavedException;

    List<Comment> findAll();

    Optional<Comment> findById(Long id);

    void deleteById(Long id);
}
