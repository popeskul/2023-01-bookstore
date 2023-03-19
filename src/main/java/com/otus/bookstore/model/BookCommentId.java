package com.otus.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookCommentId implements Serializable {
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "comment_id")
    private Long commentId;
}
