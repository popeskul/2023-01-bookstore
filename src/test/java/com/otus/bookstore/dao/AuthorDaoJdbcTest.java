package com.otus.bookstore.dao;

import com.otus.bookstore.dao.impl.AuthorDaoJdbc;
import com.otus.bookstore.service.query.author.AuthorQueryServiceImpl;
import com.otus.bookstore.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@JdbcTest
@Import({AuthorDaoJdbc.class, AuthorQueryServiceImpl.class})
class AuthorDaoJdbcTest {
    final String name = "Name";
    final String name2 = "Name22";
    final String email = "email123@mail.com";

    @Autowired
    private AuthorDaoJdbc authorDao;

    @Test
    void shouldAddAuthor() {
        Author author = Author.builder()
                .name(name)
                .email(email)
                .build();

        Optional<Integer> savedId = authorDao.insert(author);

        assertThat(savedId).isNotNull();
        assertThat(savedId.isPresent()).isTrue();

        Optional<Author> savedAuthor = authorDao.getById(savedId.get());

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.isPresent()).isTrue();
        assertEquals(savedAuthor.get().getId(), savedId.get());
        assertThat(savedAuthor.get().getName()).isEqualTo(name);
        assertThat(savedAuthor.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldUpdateAuthor() {
        Author oldAuthor = Author.builder()
                .name(name)
                .email(email)
                .build();

        Optional<Integer> savedId = authorDao.insert(oldAuthor);

        assertThat(savedId).isNotNull();
        assertThat(savedId.isPresent()).isTrue();

        Optional<Author> savedOldAuthor = authorDao.getById(savedId.get());

        assertThat(savedOldAuthor).isNotNull();
        assertThat(savedOldAuthor.isPresent()).isTrue();

        Author newAuthor = Author.builder()
                .id(savedOldAuthor.get().getId())
                .name(name2)
                .email(savedOldAuthor.get().getEmail())
                .build();

        authorDao.update(newAuthor);

        Optional<Author> updatedNewAuthor = authorDao.getById(savedId.get());

        assertThat(updatedNewAuthor).isNotNull();
        assertThat(updatedNewAuthor).isEqualTo(Optional.of(newAuthor));
    }

    @Test
    void shouldDeleteAuthorById() {
        Author author = Author.builder()
                .name(name)
                .email(email)
                .build();

        Optional<Integer> savedId = authorDao.insert(author);

        assertThat(savedId).isNotNull();
        assertThat(savedId.isPresent()).isTrue();

        Optional<Author> savedAuthor = authorDao.getById(savedId.get());

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.isPresent()).isTrue();

        authorDao.deleteById(savedId.get());

        Optional<Author> deletedAuthor = authorDao.getById(savedId.get());

        assertThat(deletedAuthor).isEmpty();
    }

    @Test
    void shouldGetAuthorById() {
        Author author = Author.builder()
                .name(name)
                .email(email)
                .build();

        Optional<Integer> savedId = authorDao.insert(author);

        assertThat(savedId).isNotNull();
        assertThat(savedId.isPresent()).isTrue();

        Optional<Author> savedAuthor = authorDao.getById(savedId.get());

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.isPresent()).isTrue();
        assertThat(savedAuthor.get().getId()).isEqualTo(savedId.get());
        assertThat(savedAuthor.get().getName()).isEqualTo(name);
        assertThat(savedAuthor.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldGetAllAuthors() {
        List<Author> before = authorDao.getAll();

        Author author = Author.builder()
                .name(name)
                .email(email)
                .build();

        Optional<Integer> savedId = authorDao.insert(author);

        assertThat(savedId).isNotNull();
        assertThat(savedId.isPresent()).isTrue();

        Optional<Author> savedAuthor = authorDao.getById(savedId.get());

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.isPresent()).isTrue();
        assertThat(savedAuthor.get().getId()).isEqualTo(savedId.get());
        assertThat(savedAuthor.get().getName()).isEqualTo(name);
        assertThat(savedAuthor.get().getEmail()).isEqualTo(email);

        List<Author> after = authorDao.getAll();

        assertThat(after.size()).isEqualTo(before.size() + 1);
    }

    @Test
    void shouldDeleteAllAuthors() {
        Author author = Author.builder()
                .name(name)
                .email(email)
                .build();

        Optional<Integer> savedId = authorDao.insert(author);

        assertThat(savedId).isNotNull();
        assertThat(savedId.isPresent()).isTrue();

        Optional<Author> savedAuthor = authorDao.getById(savedId.get());

        assertThat(savedAuthor).isNotNull();
        assertThat(savedAuthor.isPresent()).isTrue();

        authorDao.deleteAll();

        List<Author> deletedAuthors = authorDao.getAll();

        assertThat(deletedAuthors).isEmpty();
    }
}