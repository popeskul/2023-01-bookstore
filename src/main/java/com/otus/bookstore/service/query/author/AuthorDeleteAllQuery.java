package com.otus.bookstore.service.query.author;

import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;

public class AuthorDeleteAllQuery extends SqlUpdate {
    private static final String SQL_DELETE_ALL_AUTHORS = "DELETE FROM author";

    public AuthorDeleteAllQuery(DataSource dataSource) {
        super(dataSource, SQL_DELETE_ALL_AUTHORS);
    }
}
