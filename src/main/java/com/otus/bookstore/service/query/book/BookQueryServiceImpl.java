package com.otus.bookstore.service.query.book;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class BookQueryServiceImpl implements BookQueryService {
    private final DataSource dataSource;

    public BookQueryServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public BookInsertQuery getBookInsertQuery() {
        return new BookInsertQuery(dataSource);
    }

    @Override
    public BookUpdateQuery getBookUpdateQuery() {
        return new BookUpdateQuery(dataSource);
    }

    @Override
    public BookDeleteByIdQuery getBookDeleteByIdQuery() {
        return new BookDeleteByIdQuery(dataSource);
    }

    @Override
    public BookSelectAllQuery getBookSelectAllQuery() {
        return new BookSelectAllQuery(dataSource);
    }

    @Override
    public BookSelectByIdQuery getBookSelectByIdQuery() {
        return new BookSelectByIdQuery(dataSource);
    }

    @Override
    public BookDeleteAllQuery getBookDeleteAllQuery() {
        return new BookDeleteAllQuery(dataSource);
    }
}
