package com.otus.bookstore.repository;

import com.otus.bookstore.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AuthorRepositoryTest {
    private static final long ID = 1L;
    private static final String name = "Some Doe";
    private static final String name2 = "Some Doe2";
    private static final String email = "some@mail.com";
    private static final String email2 = "some2@mail.com";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final Author validAuthorWithId = Author.builder()
            .id(ID)
            .name(name)
            .email(email)
            .build();

    @Test
    void shouldCreateAuthor() {
        Author author = validAuthorWithId.toBuilder().build();

        authorRepository.save(author);

        Author actual = entityManager.find(Author.class, author.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId()).isGreaterThan(0);
        assertThat(actual.getEmail()).isEqualTo(email);
    }

    @Test
    void shouldNotCreateAuthor() {
        Author author = validAuthorWithId.toBuilder().id(0L).email(null).name(null).build();

        assertThatThrownBy(() -> authorRepository.save(author))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldUpdateAuthor() {
        Author author = validAuthorWithId.toBuilder().build();

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
    public void shouldOnFindAllAuthorsItFoundNothing() {
        entityManager.getEntityManager().createNativeQuery("DELETE FROM comment").executeUpdate();
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).isNotNull();
    }

    @Test
    public void shouldFindById() {
        Author author = validAuthorWithId.toBuilder().id(0L).build();

        Author savedAuthor = authorRepository.save(author);
        entityManager.flush();

        Optional<Author> actual = authorRepository.findById(savedAuthor.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo(name);
        assertThat(actual.get().getId()).isGreaterThan(0);
        assertThat(actual.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldNotFindAuthorById() {
        Optional<Author> author = authorRepository.findById(0L);

        assertThat(author).isEmpty();
    }

    @Test
    void shouldDeleteAuthorById() {
        // delete book first to avoid constraint violation
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
        assertThat(entityManager.find(Author.class, author.getId())).isNull();
    }

    @Test
    void shouldNotDeleteAuthorById() {
        // delete book first to avoid constraint violation
        entityManager.getEntityManager().createNativeQuery("DELETE FROM comment").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM book").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM author").executeUpdate();

        // Act
        authorRepository.deleteById(0L);

        // Assert
        assertThat(entityManager.find(Author.class, 0L)).isNull();
    }
}