package io.hexlet.blog.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@Setter
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @Lob
    @Column(nullable = false)
    @NotBlank
    private String content;
    private String author;
    private boolean published;
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt = LocalDateTime.now();
}
