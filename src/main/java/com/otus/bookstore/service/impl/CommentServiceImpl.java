package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.EntitySaveException;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.repository.CommentRepository;
import com.otus.bookstore.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Comment save(Comment comment) {
        try {
            return commentRepository.save(comment);
        } catch (Exception e) {
            throw new EntitySaveException(comment, e);
        }
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findByBookId(String bookId) {
        return bookRepository.findById(bookId).orElseThrow()
                .getComments();
    }
}
