package com.otus.bookstore.dao.impl.query.book;

public interface BookQueryService {
    BookInsertQuery getBookInsertQuery();

    BookUpdateQuery getBookUpdateQuery();

    BookDeleteByIdQuery getBookDeleteByIdQuery();

    BookSelectAllQuery getBookSelectAllQuery();

    BookSelectByIdQuery getBookSelectByIdQuery();

    BookDeleteAllQuery getBookDeleteAllQuery();
}
