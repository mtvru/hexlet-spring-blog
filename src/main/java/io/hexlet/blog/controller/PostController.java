package io.hexlet.blog.controller;

import jakarta.validation.Valid;
import io.hexlet.blog.model.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    private final List<Post> posts = new ArrayList<Post>();
    
    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue = "10") Integer limit) {
        return posts.stream().limit(limit).toList();
    }

    @PostMapping("/posts")
    public Post create(@Valid @RequestBody Post post) {
        posts.add(post);
        return post;
    }

    @GetMapping("/posts/{title}")
    public Optional<Post> show(@PathVariable String title) {
        return posts.stream()
                .filter(p -> p.getTitle().equals(title))
                .findFirst();
    }

    @PutMapping("/posts/{title}")
    public Post update(@PathVariable String title, @Valid @RequestBody Post data) {
        var maybePost = posts.stream()
                .filter(p -> p.getTitle().equals(title))
                .findFirst();
        if (maybePost.isPresent()) {
            var post = maybePost.get();
            post.setAuthor(data.getAuthor());
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
        }
        return data;
    }

    @DeleteMapping("/posts/{title}")
    public void destroy(@PathVariable String title) {
        posts.removeIf(p -> p.getTitle().equals(title));
    }
}
