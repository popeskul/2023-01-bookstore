package com.otus.bookstore.dao.impl.query.genre;

public interface GenreQueryService {
    GenreInsertQuery getGenreInsertQuery();

    GenreUpdateQuery getGenreUpdateQuery();

    GenreDeleteByIdQuery getGenreDeleteByIdQuery();

    GenreSelectAllQuery getGenreSelectAllQuery();

    GenreSelectByIdQuery getGenreSelectByIdQuery();

    GenreDeleteAllQuery getGenreDeleteAllQuery();
}
