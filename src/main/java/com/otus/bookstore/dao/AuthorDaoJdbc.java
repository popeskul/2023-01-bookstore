package com.otus.bookstore.dao;

import com.otus.bookstore.model.Author;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbcTemplate;

    public AuthorDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(Author author) {
        String sql = "INSERT INTO author (name, email) VALUES (:name, :email)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", author.getName())
                .addValue("email", author.getEmail());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public void update(Author author) {
        String sql = "UPDATE author SET name = :name, email = :email WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", author.getName())
                .addValue("email", author.getEmail())
                .addValue("id", author.getId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM author WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public Optional<Author> getById(int id) {
        String sql = "SELECT * FROM author WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.query(sql, params, new AuthorRowMapper()).stream().findFirst();
    }

    @Override
    public List<Author> getAll() {
        String sql = "SELECT * FROM author";
        return jdbcTemplate.query(sql, new AuthorRowMapper());
    }
}
