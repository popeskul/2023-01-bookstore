package com.otus.bookstore.service.impl;

import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;
import com.otus.bookstore.repository.BookCommentRepository;
import com.otus.bookstore.service.BookCommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookCommentServiceImpl implements BookCommentService {
    public static final String BOOK_COMMENT_WITH_ID_NOT_FOUND = "BookComment with id %s not found";

    private final BookCommentRepository bookCommentRepository;

    public BookCommentServiceImpl(BookCommentRepository bookCommentRepository) {
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookComment> getAll() {
        return bookCommentRepository.findAll();
    }

    @Override
    @Transactional
    public BookComment save(BookComment bookComment) {
        try {
            return bookCommentRepository.save(bookComment);
        } catch (Exception e) {
            throw new EntityNotFoundException(String.format(BOOK_COMMENT_WITH_ID_NOT_FOUND, bookComment));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookComment> getById(BookCommentId id) {
        return bookCommentRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(BookCommentId id) {
        boolean result = bookCommentRepository.deleteById(id);
        if (!result) {
            throw new EntityNotFoundException(String.format(BOOK_COMMENT_WITH_ID_NOT_FOUND, id));
        }
    }
}
