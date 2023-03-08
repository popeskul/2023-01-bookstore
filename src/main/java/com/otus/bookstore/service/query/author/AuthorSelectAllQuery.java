package com.otus.bookstore.service.query.author;

import com.otus.bookstore.model.Author;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorSelectAllQuery extends MappingSqlQuery<Author> {
    private static final String SQL_SELECT_ALL_AUTHORS = "SELECT * FROM author";

    public AuthorSelectAllQuery(DataSource dataSource) {
        super(dataSource, SQL_SELECT_ALL_AUTHORS);
    }

    @Override
    protected Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        return new Author(id, name, email);
    }
}

