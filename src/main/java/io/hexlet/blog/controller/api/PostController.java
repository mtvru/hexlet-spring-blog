package io.hexlet.blog.controller.api;

import io.hexlet.blog.dto.PostPatchDTO;
import io.hexlet.blog.dto.PostParamsDTO;
import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<PostDTO>> index(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostDTO> dtoPage = this.postService.findByPublishedTrue(page, size);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<PostDTO>> filter(PostParamsDTO params, @RequestParam(defaultValue = "1") int page) {
        Page<PostDTO> posts = this.postService.filter(params, page);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@RequestBody PostCreateDTO dto) {
        PostDTO post = this.postService.create(dto);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(post.getId())
            .toUri();
        return ResponseEntity.created(location)
            .body(post);
    }

    @GetMapping("/{id}")
    public PostDTO show(@PathVariable Long id) {
        return this.postService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(@PathVariable Long id, @RequestBody PostUpdateDTO dto) {
        PostDTO post = this.postService.update(id, dto);
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostDTO> patch(@PathVariable Long id, @RequestBody PostPatchDTO dto) {
        PostDTO post = this.postService.update(id, dto);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (!this.postService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
