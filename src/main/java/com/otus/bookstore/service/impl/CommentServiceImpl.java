package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.repository.CommentRepository;
import com.otus.bookstore.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    public static final String ERROR_NOT_FOUND_AUTHOR = "Comment with id %s not found";
    public static final String ERROR_NOT_FOUND_BOOK = "Book with id %s not found";

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        try {
            comment = commentRepository.save(comment);

            return comment;
        } catch (RuntimeException e) {
            throw new EntitySaveException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(Long id) {
        if (id <= 0) {
            throw new InvalidParameterException(String.format(ERROR_NOT_FOUND_AUTHOR, id));
        }

        Optional<Comment> commentOptional = commentRepository.findById(id);

        if (commentOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(ERROR_NOT_FOUND_AUTHOR, id));
        }

        return commentOptional;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id <= 0) {
            throw new InvalidParameterException(String.format(ERROR_NOT_FOUND_AUTHOR, id));
        }

        Optional<Comment> findComment = commentRepository.findById(id);

        if (findComment.isEmpty()) {
            throw new EntityNotFoundException(String.format(ERROR_NOT_FOUND_AUTHOR, id));
        }

        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Comment> findByBookId(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format(ERROR_NOT_FOUND_BOOK, bookId));
        }

        Book book = bookOptional.get();

        return book.getComments();
    }
}
