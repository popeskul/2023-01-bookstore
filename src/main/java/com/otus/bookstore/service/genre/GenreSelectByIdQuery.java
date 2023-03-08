package com.otus.bookstore.service.genre;

import com.otus.bookstore.model.Genre;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class GenreSelectByIdQuery extends MappingSqlQuery<Genre> {
    private static final String SQL_SELECT_GENRE_BY_ID = "SELECT id, name FROM genre WHERE id = :id";

    public GenreSelectByIdQuery(DataSource dataSource) {
        super(dataSource, SQL_SELECT_GENRE_BY_ID);
        super.declareParameter(new SqlParameter("id", Types.INTEGER));
    }

    @Override
    protected Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
