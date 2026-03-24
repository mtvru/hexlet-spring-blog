package io.hexlet.blog.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import io.hexlet.blog.dto.PostParamsDTO;
import io.hexlet.blog.model.Post;

@Component
public class PostSpecification {
    public Specification<Post> build(PostParamsDTO params) {
        return withAuthorId(params.getAuthorId())
            .and(withCreatedAtGt(params.getCreatedAtGt()));
    }

    private Specification<Post> withAuthorId(Long authorId) {
        return (root, query, cb) -> authorId == null ? cb.conjunction() : cb.equal(root.get("author").get("id"), authorId);
    }

    private Specification<Post> withCreatedAtGt(LocalDate date) {
        return (root, query, cb) -> date == null ? cb.conjunction() : cb.greaterThan(root.get("createdAt"), date);
    }
}
