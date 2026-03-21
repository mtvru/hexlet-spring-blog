package io.hexlet.blog.component;

import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostDTO toDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setPublished(post.isPublished());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setUserId(post.getUser().getId());
        return dto;
    }
}
