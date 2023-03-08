package com.otus.bookstore.dao.impl.query.book;

import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;

public class BookDeleteAllQuery extends SqlUpdate {
    private static final String SQL = "DELETE FROM book";

    public BookDeleteAllQuery(DataSource dataSource) {
        super(dataSource, SQL);
    }
}
