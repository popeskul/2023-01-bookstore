package com.otus.bookstore.service;

import com.otus.bookstore.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment save(Comment comment);

    List<Comment> getAll();

    Optional<Comment> findById(Long id);

    void deleteById(Long id);

    List<Comment> findByBookId(Long bookId);
}
