package io.hexlet.blog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class PostDTO {
    private Long id;
    private Long authorId;
    private Set<TagDTO> tags;
    private String title;
    private String content;
    private boolean published;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
