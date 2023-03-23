package com.otus.bookstore.repository;

import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository {
    List<BookComment> findAll();

    BookComment save(BookComment bookComment);

    Optional<BookComment> findById(BookCommentId id);

    boolean deleteById(BookCommentId id);
}
