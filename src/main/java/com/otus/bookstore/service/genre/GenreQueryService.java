package com.otus.bookstore.service.genre;

public interface GenreQueryService {
    GenreInsertQuery getGenreInsertQuery();

    GenreUpdateQuery getGenreUpdateQuery();

    GenreDeleteByIdQuery getGenreDeleteByIdQuery();

    GenreSelectAllQuery getGenreSelectAllQuery();

    GenreSelectByIdQuery getGenreSelectByIdQuery();

    GenreDeleteAllQuery getGenreDeleteAllQuery();
}
