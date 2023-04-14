package com.otus.bookstore.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.otus.bookstore.model.Author;
import com.otus.bookstore.model.Book;
import com.otus.bookstore.model.Comment;
import com.otus.bookstore.model.Genre;
import com.otus.bookstore.repository.AuthorRepository;
import com.otus.bookstore.repository.BookRepository;
import com.otus.bookstore.repository.CommentRepository;
import com.otus.bookstore.repository.GenreRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ChangeLog(order = "001")
public class DatabaseChangelog {
    private Author authorRowling;
    private Author authorKing;

    private Genre genreFantasy;
    private Genre genreFiction;
    private Genre genreHorror;

    private Book bookShining;

    @ChangeSet(order = "000", id = "dropDB", author = "popeskul", runAlways = true)
    public void dropDB(MongoDatabase database) {
        System.out.println("Dropping database");
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "popeskul")
    public void insertAuthors(AuthorRepository authorRepository) {
        var author1 = Author.builder().name("J.K. Rowling").email("jkrowling@gmail.com").build();
        var author2 = Author.builder().name("Stephen King").email("stephenkink@gmail.com").build();

        authorRowling = authorRepository.save(author1);
        authorKing = authorRepository.save(author2);
    }

    @ChangeSet(order = "002", id = "initGenres", author = "popeskul")
    public void insertGenres(GenreRepository genreRepository) {
        genreFantasy = genreRepository.save(Genre.builder().name("Fantasy").build());
        genreFiction = genreRepository.save(Genre.builder().name("Fiction").build());
        genreHorror = genreRepository.save(Genre.builder().name("Horror").build());
    }

    @ChangeSet(order = "003", id = "initBooks", author = "popeskul")
    public void insertBooks(BookRepository bookRepository) {
        bookShining = bookRepository.save(Book.builder()
                .title("The Shining")
                .author(authorKing)
                .genre(genreFiction)
                .price(BigDecimal.valueOf(12.99))
                .description("A horror novel by Stephen King")
                .build());
        bookRepository.save(Book.builder()
                .title("It")
                .author(authorKing)
                .genre(genreHorror)
                .price(BigDecimal.valueOf(15.99))
                .description("A horror novel by Stephen King")
                .build());
        bookRepository.save(Book.builder()
                .title("Harry Potter and the Philosopher's Stone")
                .author(authorRowling)
                .genre(genreFantasy)
                .price(BigDecimal.valueOf(9.99))
                .description("The first book in the Harry Potter series")
                .build());
        bookRepository.save(Book.builder()
                .title("The Casual Vacancy")
                .author(authorRowling)
                .genre(genreFiction)
                .price(BigDecimal.valueOf(14.99))
                .description("A novel for adults by J.K. Rowling")
                .build());
    }

    @ChangeSet(order = "004", id = "initComments", author = "popeskul")
    public void insertComments(CommentRepository commentRepository) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        commentRepository.save(Comment.builder()
                .book(bookShining)
                .text("This is a test comment.")
                .createdAt(LocalDateTime.parse("2023-03-17 12:00:00", formatter))
                .build());
        commentRepository.save(Comment.builder()
                .book(bookShining)
                .text("Another test comment.")
                .createdAt(LocalDateTime.parse("2023-03-17 12:01:00", formatter))
                .build());
    }
}
