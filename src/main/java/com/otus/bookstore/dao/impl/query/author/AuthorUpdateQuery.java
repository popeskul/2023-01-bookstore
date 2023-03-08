package com.otus.bookstore.dao.impl.query.author;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class AuthorUpdateQuery extends SqlUpdate {
    private static final String SQL_UPDATE_AUTHOR = "UPDATE author SET name = :name, email = :email WHERE id = :id";

    public AuthorUpdateQuery(DataSource dataSource) {
        super(dataSource, SQL_UPDATE_AUTHOR);
        declareParameter(new SqlParameter("id", Types.INTEGER));
        declareParameter(new SqlParameter("name", Types.VARCHAR));
        declareParameter(new SqlParameter("email", Types.VARCHAR));
    }
}
