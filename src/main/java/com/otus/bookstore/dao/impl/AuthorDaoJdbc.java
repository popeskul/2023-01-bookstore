package com.otus.bookstore.dao.impl;

import com.otus.bookstore.dao.AuthorDao;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.service.query.author.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private final AuthorInsertQuery authorInsertQuery;
    private final AuthorUpdateQuery authorUpdateQuery;
    private final AuthorDeleteByIdQuery authorDeleteByIdQuery;
    private final AuthorSelectAllQuery authorSelectAllQuery;
    private final AuthorSelectByIdQuery authorSelectByIdQuery;
    private final AuthorDeleteAllQuery authorDeleteAllQuery;

    public AuthorDaoJdbc(AuthorQueryService authorQueryService) {
        this.authorInsertQuery = authorQueryService.getAuthorInsertQuery();
        this.authorUpdateQuery = authorQueryService.getAuthorUpdateQuery();
        this.authorDeleteByIdQuery = authorQueryService.getAuthorDeleteByIdQuery();
        this.authorSelectAllQuery = authorQueryService.getAuthorSelectAllQuery();
        this.authorSelectByIdQuery = authorQueryService.getAuthorSelectByIdQuery();
        this.authorDeleteAllQuery = authorQueryService.getAuthorDeleteAllQuery();
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

        authorDeleteByIdQuery.updateByNamedParam(params);
    }

    @Override
    public void deleteAll() {
        authorDeleteAllQuery.update();
    }
}
