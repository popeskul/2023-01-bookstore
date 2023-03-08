package com.otus.bookstore.service.genre;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class GenreInsertQuery extends SqlUpdate {
    private static final String SQL_INSERT_GENRE = "INSERT INTO genre (name) VALUES (:name)";

    public GenreInsertQuery(DataSource dataSource) {
        super(dataSource, SQL_INSERT_GENRE);
        super.declareParameter(new SqlParameter(Types.VARCHAR));
        super.setGeneratedKeysColumnNames("id");
        super.setReturnGeneratedKeys(true);
    }
}
