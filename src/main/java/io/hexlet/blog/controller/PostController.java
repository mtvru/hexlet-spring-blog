package io.hexlet.blog.controller;

import io.hexlet.blog.component.PostMapper;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.repository.PostRepository;
import jakarta.validation.Valid;
import io.hexlet.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostController(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> index(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Post> posts = this.postRepository.findByPublishedTrue(pageable);
        Page<PostDTO> dtoPage = posts.map(this.postMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostCreateDTO dto) {
        Post post = this.postMapper.toEntity(dto);
        post = this.postRepository.save(post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(this.postMapper.toDTO(post));
    }

    @GetMapping("/{id}")
    public PostDTO show(@PathVariable Long id) {
        PostDTO post = this.postRepository.findById(id)
           .map(this.postMapper::toDTO)
           .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
       return post;
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(@PathVariable Long id, @Valid @RequestBody PostUpdateDTO dto) {
        Post post = this.postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        this.postMapper.updateEntity(post, dto);
        this.postRepository.save(post);
        return ResponseEntity.ok(this.postMapper.toDTO(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (!this.postRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        this.postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
