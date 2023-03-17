package com.otus.bookstore.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "book")
@Builder(toBuilder = true)
@AllArgsConstructor
@Setter
@Getter
@NamedEntityGraph(name = "book-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})
public final class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final int id;

    @Column(name = "title", nullable = false)
    private final String title;

    @Column(name = "description", nullable = false)
    private final String description;

    @Column(name = "price", nullable = false)
    private final BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    @ToString.Exclude
    private final Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    @ToString.Exclude
    private final Genre genre;

    public Book() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.price = BigDecimal.ZERO;
        this.author = null;
        this.genre = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && Objects.equals(title, book.title) && Objects.equals(description, book.description) && Objects.equals(price, book.price) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price, author, genre);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", author=" + author +
                ", genre=" + genre +
                '}';
    }
}
