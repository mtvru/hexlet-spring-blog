package io.hexlet.blog.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostParamsDTO {
    private String nameCont;
    private Long authorId;
    private LocalDate createdAtGt;
    private LocalDate createdAtLt;
}
