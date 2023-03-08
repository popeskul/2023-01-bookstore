package com.otus.bookstore.service.book;

import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int bookId = rs.getInt("id");
        final String bookTitle = rs.getString("title");
        final String bookDescription = rs.getString("description");
        final BigDecimal bookPrice = rs.getBigDecimal("price");

        final int authorId = rs.getInt("author_id");
        final String authorName = rs.getString("author_name");
        final String authorEmail = rs.getString("author_email");

        final int genreId = rs.getInt("genre_id");
        final String genreName = rs.getString("genre_name");

        final Author author = Author.builder()
                .id(authorId)
                .name(authorName)
                .email(authorEmail)
                .build();

        final Genre genre = Genre.builder()
                .id(genreId)
                .name(genreName)
                .build();

        return Book.builder()
                .id(bookId)
                .title(bookTitle)
                .description(bookDescription)
                .price(bookPrice)
                .author(author)
                .genre(genre)
                .build();
    }
}
