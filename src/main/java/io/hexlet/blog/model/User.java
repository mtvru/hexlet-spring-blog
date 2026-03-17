package io.hexlet.blog.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class User {
    private Long id;
    private String name;
    @NotBlank
    private String email;
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt = LocalDateTime.now();
}
