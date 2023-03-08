package com.otus.bookstore.service.book;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class BookInsertQuery extends SqlUpdate {
    static final String SQL_INSERT_BOOK = "INSERT INTO book (title, description, price, author_id, genre_id) VALUES (:title, :description, :price, :author_id, :genre_id)";

    public BookInsertQuery(DataSource dataSource) {
        super(dataSource, SQL_INSERT_BOOK);
        super.declareParameter(new SqlParameter("title", Types.VARCHAR));
        super.declareParameter(new SqlParameter("description", Types.VARCHAR));
        super.declareParameter(new SqlParameter("price", Types.NUMERIC));
        super.declareParameter(new SqlParameter("author_id", Types.INTEGER));
        super.declareParameter(new SqlParameter("genre_id", Types.INTEGER));
        super.setGeneratedKeysColumnNames("id");
        super.setReturnGeneratedKeys(true);
    }
}
