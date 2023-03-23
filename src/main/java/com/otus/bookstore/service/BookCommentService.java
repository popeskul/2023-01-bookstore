package com.otus.bookstore.service;

import com.otus.bookstore.model.BookComment;
import com.otus.bookstore.model.BookCommentId;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    List<BookComment> getAll();

    BookComment save(BookComment bookComment);

    Optional<BookComment> getById(BookCommentId id);

    void deleteById(BookCommentId id);
}
