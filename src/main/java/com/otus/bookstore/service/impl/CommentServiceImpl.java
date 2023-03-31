package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.repository.CommentRepository;
import com.otus.bookstore.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    public static final String ERROR_NOT_FOUND_AUTHOR = "Comment with id %s not found";
    public static final String ERROR_NOT_FOUND_BOOK = "Book with id %s not found";
    public static final String ERROR_COMMENT_NULL = "Comment is null";
    public static final String ERROR_COMMENT_ID_MUST_NOT_BE_SET = "Comment id must not be set";

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public Comment create(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException(ERROR_COMMENT_NULL);
        }

        if (comment.getId() != 0) {
            throw new EntitySaveException(ERROR_COMMENT_ID_MUST_NOT_BE_SET);
        }

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException(ERROR_COMMENT_NULL);
        }

        if (comment.getId() != 0) {
            throw new EntitySaveException(ERROR_COMMENT_ID_MUST_NOT_BE_SET);
        }

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format(ERROR_NOT_FOUND_AUTHOR, id));
        }

        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format(ERROR_NOT_FOUND_AUTHOR, id));
        }

        commentRepository.findById(id).orElseThrow();

        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow()
                .getComments();
    }
}
