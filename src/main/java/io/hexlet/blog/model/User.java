package io.hexlet.blog.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    @NotBlank
    private String email;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
