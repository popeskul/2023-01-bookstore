package com.otus.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "comment")
@Getter
@Builder(toBuilder = true)
@ToString
@AllArgsConstructor
@NamedEntityGraph(name = "comment-entity-graph", attributeNodes = {
        @NamedAttributeNode("book")
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

    @Column(name = "text", nullable = false)
    private final String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public List<Comment> getComments() {
        if (book == null || book.getComments() == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(book.getComments());
    }

    public Comment() {
        this.id = 0L;
        this.book = null;
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
        return Objects.equals(id, comment.id) && Objects.equals(book, comment.book) && Objects.equals(text, comment.text) && Objects.equals(createdAt, comment.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, text, createdAt);
    }
}
