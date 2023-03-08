package com.otus.bookstore.dao.impl;

import com.otus.bookstore.dao.GenreDao;
import com.otus.bookstore.dao.impl.query.genre.*;
import com.otus.bookstore.model.Genre;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class GenreDaoJdbc implements GenreDao {
    private final GenreInsertQuery genreQueryService;
    private final GenreUpdateQuery genreUpdateQuery;
    private final GenreDeleteByIdQuery genreDeleteByIdQuery;
    private final GenreSelectAllQuery genreSelectAllQuery;
    private final GenreSelectByIdQuery genreSelectByIdQuery;
    private final GenreDeleteAllQuery genreDeleteAllQuery;

    public GenreDaoJdbc(GenreQueryService genreQueryService) {
        this.genreQueryService = genreQueryService.getGenreInsertQuery();
        this.genreUpdateQuery = genreQueryService.getGenreUpdateQuery();
        this.genreDeleteByIdQuery = genreQueryService.getGenreDeleteByIdQuery();
        this.genreSelectAllQuery = genreQueryService.getGenreSelectAllQuery();
        this.genreSelectByIdQuery = genreQueryService.getGenreSelectByIdQuery();
        this.genreDeleteAllQuery = genreQueryService.getGenreDeleteAllQuery();
    }

    @Override
    public Optional<Integer> insert(final Genre genre) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", genre.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        genreQueryService.updateByNamedParam(params, keyHolder);
        return Optional.of(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void update(Genre genre) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", genre.getId());
        params.put("name", genre.getName());

        genreUpdateQuery.updateByNamedParam(params);
    }

    @Override
    public void deleteById(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);

        genreDeleteByIdQuery.updateByNamedParam(params);
    }

    @Override
    public Optional<Genre> getById(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);

        return Optional.ofNullable(genreSelectByIdQuery.findObjectByNamedParam(params));
    }

    @Override
    public List<Genre> getAll() {
        return genreSelectAllQuery.execute();
    }

    @Override
    public void deleteAll() {
        genreDeleteAllQuery.updateByNamedParam(new HashMap<>());
    }
}
