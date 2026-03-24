package io.hexlet.blog.service;

import io.hexlet.blog.dto.*;
import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.mapper.PostMapper;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.repository.PostRepository;
import io.hexlet.blog.specification.PostSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class PostService {
    private final PostRepository repository;
    private final PostMapper mapper;
    private final PostSpecification specBuilder;

    public PostService(PostRepository repository, PostMapper mapper, PostSpecification specBuilder) {
        this.repository = repository;
        this.mapper = mapper;
        this.specBuilder = specBuilder;
    }

    public Page<PostDTO> findByPublishedTrue(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Post> posts = this.repository.findByPublishedTrue(pageable);
        return posts.map(this.mapper::map);
    }

    public Page<PostDTO> filter(PostParamsDTO params, int page) {
        Specification<Post> spec = this.specBuilder.build(params);
        Page<Post> posts = this.repository.findAll(spec, PageRequest.of(page - 1, 10));
        return posts.map(this.mapper::map);
    }

    public PostDTO create(PostCreateDTO dto) {
        Post post = this.mapper.map(dto);
        post = this.repository.save(post);
        return this.mapper.map(post);
    }

    public PostDTO findById(Long id) {
        return this.repository.findById(id)
            .map(this.mapper::map)
            .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    public PostDTO update(Long id, PostUpdateDTO dto) {
        Post post = this.repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        this.mapper.update(dto, post);
        this.repository.save(post);
        return this.mapper.map(post);
    }

    public PostDTO update(Long id, PostPatchDTO dto) {
        Post post = this.repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
        this.mapper.update(dto, post);
        this.repository.save(post);
        return this.mapper.map(post);
    }

    public boolean delete(Long id) {
        if (!this.repository.existsById(id)) {
            return false;
        }
        this.repository.deleteById(id);
        return true;
    }
}
