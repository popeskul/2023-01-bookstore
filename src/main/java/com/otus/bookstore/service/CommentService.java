package com.otus.bookstore.service;

import com.otus.bookstore.exception.CommentErrorSavedException;
import com.otus.bookstore.exception.CommentNotFoundException;
import com.otus.bookstore.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment save(Comment comment) throws CommentErrorSavedException;
    List<Comment> getAll();
    Optional<Comment> findById(Long id) throws CommentNotFoundException;
    void deleteById(Long id) throws CommentNotFoundException;
}
