package com.otus.bookstore.service.book;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class BookUpdateQuery extends SqlUpdate {
    private static final String SQL_UPDATE_BOOK = "UPDATE book SET title = :title, description = :description, price = :price, author_id = :author_id, genre_id = :genre_id WHERE id = :id";

    public BookUpdateQuery(DataSource dataSource) {
        super(dataSource, SQL_UPDATE_BOOK);
        super.declareParameter(new SqlParameter("id", Types.INTEGER));
        super.declareParameter(new SqlParameter("title", Types.VARCHAR));
        super.declareParameter(new SqlParameter("description", Types.VARCHAR));
        super.declareParameter(new SqlParameter("price", Types.NUMERIC));
        super.declareParameter(new SqlParameter("author_id", Types.INTEGER));
        super.declareParameter(new SqlParameter("genre_id", Types.INTEGER));
        super.setGeneratedKeysColumnNames("id");
        super.setReturnGeneratedKeys(true);
        super.compile();
    }
}
