package com.otus.bookstore.repository;

import com.otus.bookstore.model.Author;
import com.otus.bookstore.repository.impl.AuthorRepositoryJpa;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({AuthorRepositoryJpa.class})
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
    void shouldSaveAuthor() {
        Author author = validAuthorWithId.toBuilder().build();

        authorRepository.save(author);

        Author actual = entityManager.find(Author.class, author.getId());

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getId()).isGreaterThan(0);
        assertThat(actual.getEmail()).isEqualTo(email);
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
    public void shouldFindById() {
        Author author = validAuthorWithId.toBuilder().id(0L).build();
        authorRepository.save(author);

        entityManager.clear();

        Optional<Author> foundAuthor = authorRepository.findById(author.getId());

        assertThat(foundAuthor).isPresent().get().isEqualTo(author);
    }

    @Test
    void shouldNotFindAuthorById() {
        assertThrows(EntityNotFoundException.class, () -> authorRepository.findById(-1));
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
        assertThrows(EntityNotFoundException.class, () -> authorRepository.findById(author.getId()));
    }
}