package com.otus.bookstore.service.author;

import com.otus.bookstore.model.Author;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AuthorSelectByIdQuery extends MappingSqlQuery<Author> {
    private static final String SQL_SELECT_AUTHOR_BY_ID = "SELECT * FROM author WHERE id = :id";

    public AuthorSelectByIdQuery(DataSource dataSource) {
        super(dataSource, SQL_SELECT_AUTHOR_BY_ID);
        declareParameter(new SqlParameter("id", Types.INTEGER));
    }

    @Override
    protected Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        return new Author(id, name, email);
    }
}
