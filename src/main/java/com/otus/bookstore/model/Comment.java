package com.otus.bookstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comment")
@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NamedEntityGraph(name = "comment-entity-graph", attributeNodes = {
        @NamedAttributeNode("book"),
        @NamedAttributeNode("author")
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private final Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @ToString.Exclude
    private final Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private final Author author;

    @Column(name = "text", nullable = false)
    private final String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Comment() {
        this.id = 0L;
        this.book = null;
        this.author = null;
        this.text = "";
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(book, comment.book) && Objects.equals(author, comment.author) && Objects.equals(text, comment.text) && Objects.equals(createdAt, comment.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, author, text, createdAt);
    }
}
