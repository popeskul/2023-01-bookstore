package com.otus.bookstore.service.query.genre;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class GenreDeleteByIdQuery extends SqlUpdate {
    private static final String SQL_DELETE_GENRE_BY_ID = "DELETE FROM genre WHERE id = :id";

    public GenreDeleteByIdQuery(DataSource dataSource) {
        super(dataSource, SQL_DELETE_GENRE_BY_ID);
        declareParameter(new SqlParameter("id", Types.INTEGER));
    }
}
