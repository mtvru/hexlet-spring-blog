package io.hexlet.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Setter
@Getter
public class UserPatchDTO {
    private JsonNullable<String> firstName = JsonNullable.undefined();
    private JsonNullable<String> lastName = JsonNullable.undefined();
    private JsonNullable<LocalDate> birthday = JsonNullable.undefined();
    @NotBlank
    private JsonNullable<String> email = JsonNullable.undefined();
    private JsonNullable<String> password = JsonNullable.undefined();
}
