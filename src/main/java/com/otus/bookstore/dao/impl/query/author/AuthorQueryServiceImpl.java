package com.otus.bookstore.dao.impl.query.author;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class AuthorQueryServiceImpl implements AuthorQueryService {
    private final DataSource dataSource;

    public AuthorQueryServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public AuthorInsertQuery getAuthorInsertQuery() {
        return new AuthorInsertQuery(dataSource);
    }

    @Override
    public AuthorUpdateQuery getAuthorUpdateQuery() {
        return new AuthorUpdateQuery(dataSource);
    }

    @Override
    public AuthorDeleteByIdQuery getAuthorDeleteByIdQuery() {
        return new AuthorDeleteByIdQuery(dataSource);
    }

    @Override
    public AuthorSelectAllQuery getAuthorSelectAllQuery() {
        return new AuthorSelectAllQuery(dataSource);
    }

    @Override
    public AuthorSelectByIdQuery getAuthorSelectByIdQuery() {
        return new AuthorSelectByIdQuery(dataSource);
    }

    @Override
    public AuthorDeleteAllQuery getAuthorDeleteAllQuery() {
        return new AuthorDeleteAllQuery(dataSource);
    }
}
