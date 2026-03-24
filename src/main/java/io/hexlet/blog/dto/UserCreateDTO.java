package io.hexlet.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
