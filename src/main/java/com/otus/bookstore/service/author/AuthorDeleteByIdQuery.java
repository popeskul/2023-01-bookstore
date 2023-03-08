package com.otus.bookstore.service.author;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class AuthorDeleteByIdQuery extends SqlUpdate {
    private static final String SQL_DELETE_AUTHOR_BY_ID = "DELETE FROM author WHERE id = :id";

    public AuthorDeleteByIdQuery(DataSource dataSource) {
        super(dataSource, SQL_DELETE_AUTHOR_BY_ID);
        declareParameter(new SqlParameter("id", Types.INTEGER));
    }
}
