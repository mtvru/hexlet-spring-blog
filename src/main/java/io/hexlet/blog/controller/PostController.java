package io.hexlet.blog.controller;

import io.hexlet.blog.repository.PostRepository;
import jakarta.validation.Valid;
import io.hexlet.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
        Pageable pageable = PageRequest.ofSize(limit);
        return ResponseEntity.ok(this.postRepository.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody Post post) {
        post.setId(null);
        post = this.postRepository.save(post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable Long id) {
       Optional<Post> post = this.postRepository.findById(id);
       return ResponseEntity.of(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @Valid @RequestBody Post data) {
        Optional<Post> maybePost = this.postRepository.findById(id);
        if (maybePost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Post post = maybePost.get();
        post.setAuthor(data.getAuthor());
        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setPublished(data.isPublished());
        this.postRepository.save(post);
        return ResponseEntity.ok(post);
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
