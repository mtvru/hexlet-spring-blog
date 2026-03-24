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
            .and(withNameCont(params.getTitleCont()))
            .and(withCreatedAtGt(params.getCreatedAtGt()))
            .and(withCreatedAtLt(params.getCreatedAtLt()));
    }

    private Specification<Post> withAuthorId(Long authorId) {
        return (root, query, cb) -> authorId == null ? cb.conjunction() : cb.equal(root.get("author").get("id"), authorId);
    }

    private Specification<Post> withNameCont(String nameCont) {
        return (root, query, cb) -> nameCont == null ? cb.conjunction() : cb.like(cb.lower(root.get("name")), "%" + nameCont.toLowerCase() + "%");
    }

    private Specification<Post> withCreatedAtGt(LocalDate date) {
        return (root, query, cb) -> date == null ? cb.conjunction() : cb.greaterThan(root.get("createdAt"), date.atStartOfDay());
    }

    private Specification<Post> withCreatedAtLt(LocalDate date) {
        return (root, query, cb) -> date == null ? cb.conjunction() : cb.lessThan(root.get("createdAt"), date.atStartOfDay());
    }
}
