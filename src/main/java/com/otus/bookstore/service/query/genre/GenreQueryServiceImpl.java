package com.otus.bookstore.service.query.genre;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class GenreQueryServiceImpl implements GenreQueryService {
    private final DataSource dataSource;

    public GenreQueryServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public GenreInsertQuery getGenreInsertQuery() {
        return new GenreInsertQuery(dataSource);
    }

    @Override
    public GenreUpdateQuery getGenreUpdateQuery() {
        return new GenreUpdateQuery(dataSource);
    }

    @Override
    public GenreDeleteByIdQuery getGenreDeleteByIdQuery() {
        return new GenreDeleteByIdQuery(dataSource);
    }

    @Override
    public GenreSelectAllQuery getGenreSelectAllQuery() {
        return new GenreSelectAllQuery(dataSource);
    }

    @Override
    public GenreSelectByIdQuery getGenreSelectByIdQuery() {
        return new GenreSelectByIdQuery(dataSource);
    }

    @Override
    public GenreDeleteAllQuery getGenreDeleteAllQuery() {
        return new GenreDeleteAllQuery(dataSource);
    }
}
