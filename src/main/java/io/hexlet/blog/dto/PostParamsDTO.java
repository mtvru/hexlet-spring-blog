package io.hexlet.blog.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostParamsDTO {
    private String titleCont;
    private Long authorId;
    private LocalDate createdAtGt;
    private LocalDate createdAtLt;
}
