package io.hexlet.blog.controller;

import io.hexlet.blog.dto.TagCreateDTO;
import io.hexlet.blog.dto.TagDTO;
import io.hexlet.blog.dto.TagPatchDTO;
import io.hexlet.blog.dto.TagUpdateDTO;
import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.mapper.TagMapper;
import io.hexlet.blog.model.Tag;
import io.hexlet.blog.repository.TagRepository;
import io.hexlet.blog.repository.PostRepository;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final PostRepository postRepository;

    public TagController(TagRepository tagRepository, TagMapper tagMapper, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.postRepository = postRepository;
    }

    @GetMapping
    public ResponseEntity<Page<TagDTO>> index(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Tag> tags = this.tagRepository.findAll(pageable);
        Page<TagDTO> dtoPage = tags.map(this.tagMapper::map);
        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagCreateDTO dto) {
        Tag tag = this.tagMapper.map(dto);
        tag = this.tagRepository.save(tag);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tag.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(this.tagMapper.map(tag));
    }

    @GetMapping("/{id}")
    public TagDTO show(@PathVariable Long id) {
        TagDTO tag = this.tagRepository.findById(id)
           .map(this.tagMapper::map)
           .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));
       return tag;
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> update(@PathVariable Long id, @Valid @RequestBody TagUpdateDTO dto) {
        Tag tag = this.tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));
        this.tagMapper.update(dto, tag);
        this.tagRepository.save(tag);
        return ResponseEntity.ok(this.tagMapper.map(tag));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TagDTO> patchTag(@PathVariable Long id, @RequestBody TagPatchDTO dto) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));
        this.tagMapper.update(dto, tag);
        this.tagRepository.save(tag);
        return ResponseEntity.ok(this.tagMapper.map(tag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (!this.tagRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        this.tagRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
