package com.otus.bookstore.service;

import com.otus.bookstore.exception.BookCommentErrorSavedException;
import com.otus.bookstore.exception.BookCommentNotFoundException;
import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    List<BookComment> getAll();

    BookComment save(BookComment bookComment) throws BookCommentErrorSavedException;

    Optional<BookComment> getById(BookCommentId id) throws BookCommentNotFoundException;

    void deleteById(BookCommentId id) throws BookCommentNotFoundException;
}
