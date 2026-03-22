package io.hexlet.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TagPatchDTO {
    @NotBlank
    @Size(min = 2)
    private JsonNullable<String> name = JsonNullable.undefined();
}
