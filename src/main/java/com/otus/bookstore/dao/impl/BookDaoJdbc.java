package com.otus.bookstore.dao.impl;

import com.otus.bookstore.dao.BookDao;
import com.otus.bookstore.dao.impl.query.book.*;
import com.otus.bookstore.model.Book;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BookDaoJdbc implements BookDao {

    private final BookInsertQuery bookInsertQuery;
    private final BookDeleteAllQuery bookDeleteAllQuery;
    private final BookDeleteByIdQuery bookDeleteByIdQuery;
    private final BookSelectAllQuery bookSelectAllQuery;
    private final BookSelectByIdQuery bookSelectByIdQuery;
    private final BookUpdateQuery bookUpdateQuery;

    public BookDaoJdbc(BookQueryService bookQueryService) {
        this.bookInsertQuery = bookQueryService.getBookInsertQuery();
        this.bookDeleteAllQuery = bookQueryService.getBookDeleteAllQuery();
        this.bookDeleteByIdQuery = bookQueryService.getBookDeleteByIdQuery();
        this.bookSelectAllQuery = bookQueryService.getBookSelectAllQuery();
        this.bookSelectByIdQuery = bookQueryService.getBookSelectByIdQuery();
        this.bookUpdateQuery = bookQueryService.getBookUpdateQuery();
    }

    @Override
    public Optional<Integer> insert(Book book) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("title", book.getTitle());
        params.put("description", book.getDescription());
        params.put("price", book.getPrice());
        params.put("author_id", book.getAuthor().getId());
        params.put("genre_id", book.getGenre().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        bookInsertQuery.updateByNamedParam(params, keyHolder);

        return Optional.of(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void update(Book book) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        params.put("title", book.getTitle());
        params.put("description", book.getDescription());
        params.put("price", book.getPrice());
        params.put("author_id", book.getAuthor().getId());
        params.put("genre_id", book.getGenre().getId());

        bookUpdateQuery.updateByNamedParam(params);
    }

    @Override
    public void deleteById(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);

        bookDeleteByIdQuery.updateByNamedParam(params);
    }

    @Override
    public Optional<Book> getById(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);

        return Optional.ofNullable(bookSelectByIdQuery.findObjectByNamedParam(params));
    }

    @Override
    public List<Book> getAll() {
        return bookSelectAllQuery.execute();
    }

    @Override
    public void deleteAll() {
        bookDeleteAllQuery.updateByNamedParam(new HashMap<>());
    }
}
