package com.otus.bookstore.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Objects;

@Entity
@Table(name = "book_comment")
@ToString
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@NamedEntityGraph(name = "book-comment-entity-graph", attributeNodes = {
        @NamedAttributeNode("book"),
        @NamedAttributeNode("comment")
})
public class BookComment {
    @EmbeddedId
    private BookCommentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @Fetch(FetchMode.JOIN)
    @ToString.Exclude
    private Book book;

    // TODO: 19.03.2023: maybe CascadeType.REMOVE after deleting bookComment
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commentId")
    @Fetch(FetchMode.JOIN)
    @ToString.Exclude
    private Comment comment;

    public BookComment(BookCommentId id, Book book, Comment comment) {
        this.id = id;
        this.book = book;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookComment that = (BookComment) o;
        return Objects.equals(id, that.id) && Objects.equals(book, that.book) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, comment);
    }
}
