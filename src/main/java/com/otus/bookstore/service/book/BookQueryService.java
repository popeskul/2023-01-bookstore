package com.otus.bookstore.service.book;

public interface BookQueryService {
    BookInsertQuery getBookInsertQuery();

    BookUpdateQuery getBookUpdateQuery();

    BookDeleteByIdQuery getBookDeleteByIdQuery();

    BookSelectAllQuery getBookSelectAllQuery();

    BookSelectByIdQuery getBookSelectByIdQuery();

    BookDeleteAllQuery getBookDeleteAllQuery();
}
