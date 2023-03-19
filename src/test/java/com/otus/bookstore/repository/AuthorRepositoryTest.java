package com.otus.bookstore.repository;

import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.impl.AuthorRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AuthorRepositoryJpa.class})
class AuthorRepositoryTest {
    private static final String name = "Some Doe";
    private static final String name2 = "Some Doe2";
    private static final String email = "some@mail.com";
    private static final String email2 = "some2@mail.com";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAuthor() {
        Author author = Author.builder()
                .name(name)
                .email(email)
                .build();

        authorRepository.save(author);

        Author actual = entityManager.find(Author.class, author.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId()).isGreaterThan(0);
        assertThat(actual.getEmail()).isEqualTo(email);
    }

    @Test
    void shouldUpdateAuthor() {
        Author author = Author.builder()
                .name(name)
                .email(email)
                .build();

        authorRepository.save(author);

        Author actual = entityManager.find(Author.class, author.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId()).isGreaterThan(0);
        assertThat(actual.getEmail()).isEqualTo(email);

        Author dirty = actual.toBuilder().name(name2).email(email2).build();

        authorRepository.save(dirty);

        Author updated = entityManager.find(Author.class, author.getId());

        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(name2);
        assertThat(updated.getId()).isGreaterThan(0);
        assertThat(updated.getEmail()).isEqualTo(email2);
    }

    @Test
    public void shouldGetAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).isNotNull();
    }

    @Test
    public void shouldFindById() {
        Author author = Author.builder()
                .name(name)
                .email(email)
                .build();
        authorRepository.save(author);

        entityManager.clear();

        Optional<Author> foundAuthor = authorRepository.findById(author.getId());

        assertThat(foundAuthor).isPresent().get().isEqualTo(author);
    }

    @Test
    void shouldNotFindAuthorById() {
        Optional<Author> foundAuthor = authorRepository.findById(-1);
        assertThat(foundAuthor).isEmpty();
    }

    @Test
    void shouldDeleteAuthorById() {
        // delete book first to avoid constraint violation
        entityManager.getEntityManager().createNativeQuery("DELETE FROM book_comment").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM comment").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM book").executeUpdate();

        // Arrange
        Author author = Author.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        entityManager.persist(author);
        entityManager.flush();

        // Act
        authorRepository.deleteById(author.getId());

        // Assert
        Optional<Author> deletedAuthor = authorRepository.findById(author.getId());
        assertThat(deletedAuthor).isEmpty();
    }
}