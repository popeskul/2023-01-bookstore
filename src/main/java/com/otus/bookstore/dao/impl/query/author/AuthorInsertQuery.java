package com.otus.bookstore.dao.impl.query.author;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class AuthorInsertQuery extends SqlUpdate {
    private static final String SQL_INSERT_AUTHOR = "INSERT INTO author (name, email) VALUES (:name, :email)";

    public AuthorInsertQuery(DataSource dataSource) {
        super(dataSource, SQL_INSERT_AUTHOR);
        super.declareParameter(new SqlParameter(Types.VARCHAR));
        super.declareParameter(new SqlParameter(Types.VARCHAR));
        super.setGeneratedKeysColumnNames("id");
        super.setReturnGeneratedKeys(true);
    }
}
