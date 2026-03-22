package io.hexlet.blog.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    @Column(name = "email", unique = true, nullable = false, length = 200)
    private String email;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
        post.setAuthor(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setAuthor(null);
    }
}
