package com.otus.bookstore.repository;

import com.otus.bookstore.exception.BookCommentDeleteException;
import com.otus.bookstore.exception.BookCommentErrorSavedException;
import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository {
    List<BookComment> findAll();

    BookComment save(BookComment bookComment) throws BookCommentErrorSavedException;

    Optional<BookComment> findById(BookCommentId id);

    void deleteByBookCommentId(BookCommentId id) throws BookCommentDeleteException;
}
