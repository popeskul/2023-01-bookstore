package com.otus.bookstore.service.author;

public interface AuthorQueryService {
    AuthorInsertQuery getAuthorInsertQuery();

    AuthorUpdateQuery getAuthorUpdateQuery();

    AuthorDeleteByIdQuery getAuthorDeleteByIdQuery();

    AuthorSelectAllQuery getAuthorSelectAllQuery();

    AuthorSelectByIdQuery getAuthorSelectByIdQuery();

    AuthorDeleteAllQuery getAuthorDeleteAllQuery();
}
