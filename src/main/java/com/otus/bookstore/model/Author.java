package com.otus.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Entity
@Table(name = "author")
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public final class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private final Long id;

    @Column(name = "name", nullable = false)
    private final String name;

    @Column(name = "email", nullable = false)
    private final String email;

    public Author() {
        this.id = 0L;
        this.name = "";
        this.email = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Author author = (Author) o;
        return id == author.id && Objects.equals(name, author.name) && Objects.equals(email, author.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
