package com.otus.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
public final class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final Long id;

    @Column(name = "title", nullable = false)
    private final String title;

    @Column(name = "description", nullable = false)
    private final String description;

    @Column(name = "price", nullable = false)
    private final BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private final Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    @ToString.Exclude
    private final Genre genre;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    @Fetch(FetchMode.JOIN)
    private List<Comment> comments;

    public Book() {
        this.id = 0L;
        this.title = "";
        this.description = "";
        this.price = BigDecimal.ZERO;
        this.author = null;
        this.genre = null;
    }

    public List<Comment> getComments() {
        return List.copyOf(comments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(description, book.description) && Objects.equals(price, book.price) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre);
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
