package com.otus.bookstore.service.impl;

import com.otus.bookstore.exception.BookCommentErrorSavedException;
import com.otus.bookstore.exception.BookCommentNotFoundException;
import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;
import com.otus.bookstore.repository.BookCommentRepository;
import com.otus.bookstore.service.BookCommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;

    public BookCommentServiceImpl(BookCommentRepository bookCommentRepository) {
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    public List<BookComment> getAll() {
        return bookCommentRepository.findAll();
    }

    @Override
    public BookComment save(BookComment bookComment) throws BookCommentErrorSavedException {
        try {
            return bookCommentRepository.save(bookComment);
        } catch (Exception e) {
            throw new BookCommentErrorSavedException();
        }
    }

    @Override
    public Optional<BookComment> getById(BookCommentId id) throws BookCommentNotFoundException {
        try {
            Optional<BookComment> bookCommentOptional = bookCommentRepository.findById(id);

            if (bookCommentOptional.isEmpty()) {
                throw new BookCommentNotFoundException(id.getBookId(), id.getCommentId());
            }

            return bookCommentOptional;
        } catch (RuntimeException e) {
            throw new BookCommentNotFoundException(id.getBookId(), id.getCommentId());
        }
    }

    @Override
    public void deleteById(BookCommentId id) throws BookCommentNotFoundException {
        try {
            bookCommentRepository.deleteByBookCommentId(id);
        } catch (RuntimeException e) {
            throw new BookCommentNotFoundException(id.getBookId(), id.getCommentId());
        }
    }
}
