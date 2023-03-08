package com.otus.bookstore.service.book;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class BookDeleteByIdQuery extends SqlUpdate {
    private static final String SQL = "DELETE FROM book WHERE id = :id";

    public BookDeleteByIdQuery(DataSource dataSource) {
        super(dataSource, SQL);
        super.declareParameter(new SqlParameter("id", Types.INTEGER));
    }
}
