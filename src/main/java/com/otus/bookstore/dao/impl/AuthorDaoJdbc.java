package com.otus.bookstore.dao;

import com.otus.bookstore.model.Author;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbcTemplate;
    private DataSource dataSource;
    private AuthorInsertQuery authorInsertQuery;
    private AuthorUpdateQuery authorUpdateQuery;
    private DeleteAuthor deleteAuthor;
    private AuthorSelectAllQuery authorSelectAllQuery;
    private AuthorSelectByIdQuery authorSelectByIdQuery;
    private DeleteAllAuthor deleteAllAuthor;

    public AuthorDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.authorInsertQuery = new AuthorInsertQuery(dataSource);
        this.authorUpdateQuery = new AuthorUpdateQuery(dataSource);
        this.deleteAuthor = new DeleteAuthor(dataSource);
        this.authorSelectAllQuery = new AuthorSelectAllQuery(dataSource);
        this.authorSelectByIdQuery = new AuthorSelectByIdQuery(dataSource);
        this.deleteAllAuthor = new DeleteAllAuthor(dataSource);
    }

    @Override
    public Optional<Integer> insert(Author author) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", author.getName());
        params.put("email", author.getEmail());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        authorInsertQuery.updateByNamedParam(params, keyHolder);
        return Optional.of(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public Optional<Author> getById(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);

        return Optional.ofNullable(authorSelectByIdQuery.findObjectByNamedParam(params));
    }

    @Override
    public List<Author> getAll() {
        return authorSelectAllQuery.execute();
    }

    @Override
    public void update(Author author) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", author.getId());
        params.put("name", author.getName());
        params.put("email", author.getEmail());

        authorUpdateQuery.updateByNamedParam(params);
    }

    @Override
    public void deleteById(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);

        deleteAuthor.updateByNamedParam(params);
    }

    @Override
    public void deleteAll() {
        deleteAllAuthor.update();
    }
}
