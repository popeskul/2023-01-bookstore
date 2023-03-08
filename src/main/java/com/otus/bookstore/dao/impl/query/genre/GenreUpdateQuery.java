package com.otus.bookstore.dao.impl.query.genre;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.sql.DataSource;
import java.sql.Types;

public class GenreUpdateQuery extends SqlUpdate {
    private static final String SQL_UPDATE_GENRE = "UPDATE genre SET name = :name WHERE id = :id";

    public GenreUpdateQuery(DataSource dataSource) {
        super(dataSource, SQL_UPDATE_GENRE);
        declareParameter(new SqlParameter("id", Types.INTEGER));
        declareParameter(new SqlParameter("name", Types.VARCHAR));
    }
}
