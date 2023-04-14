package com.otus.bookstore.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Document
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public final class Book {

    @Id
    private String id;

    @Field(name = "title")
    @NotBlank(message = "Title is mandatory")
    private String title;

    @Field(name = "description")
    @NotBlank(message = "Description is mandatory")
    private String description;

    @Field(name = "price")
    @Min(value = 0, message = "Price must be greater than 0")
    private BigDecimal price;

    @DBRef
    @NotNull(message = "Author is mandatory")
    @ToString.Exclude
    private Author author;

    @DBRef
    @ToString.Exclude
    @NotNull(message = "Genre is mandatory")
    private Genre genre;

    @DBRef
    private List<Comment> comments;

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
