package io.hexlet.blog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PostDTO {
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private boolean published;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
