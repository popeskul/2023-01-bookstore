package com.otus.bookstore.service.genre;

import com.otus.bookstore.model.Genre;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreSelectAllQuery extends MappingSqlQuery<Genre> {
    private static final String SQL_SELECT_GENRE_ALL = "SELECT * FROM genre";

    public GenreSelectAllQuery(DataSource dataSource) {
        super(dataSource, SQL_SELECT_GENRE_ALL);
    }

    @Override
    protected Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
