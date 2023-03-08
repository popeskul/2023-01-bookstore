package com.otus.bookstore.service.query.genre;

import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;

public class GenreDeleteAllQuery extends SqlUpdate {
    private static final String SQL_DELETE_ALL_GENRES = "DELETE FROM genre";

    public GenreDeleteAllQuery(DataSource dataSource) {
        super(dataSource, SQL_DELETE_ALL_GENRES);
    }
}
