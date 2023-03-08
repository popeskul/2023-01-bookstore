package com.otus.bookstore.service.query.book;

import com.otus.bookstore.model.Book;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookSelectAllQuery extends MappingSqlQuery<Book> {
    private static final String SQL_SELECT_ALL = "SELECT b.id, b.title, b.description, b.price, a.id as author_id, a.name as author_name, a.email as author_email, g.id as genre_id, g.name as genre_name FROM book " +
            "b INNER JOIN author a ON b.author_id = a.id " +
            "INNER JOIN genre g ON b.genre_id = g.id";

    public BookSelectAllQuery(DataSource dataSource) {
        super(dataSource, SQL_SELECT_ALL);
        super.compile();
    }

    @Override
    protected Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BookRowMapper().mapRow(rs, rowNum);
    }
}
