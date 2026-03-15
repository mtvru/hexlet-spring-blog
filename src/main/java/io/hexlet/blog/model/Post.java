package io.hexlet.blog.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class Post {
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank
    private String content;
    private String author;
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt = LocalDateTime.now();
}
