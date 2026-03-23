package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.PostPatchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import io.hexlet.blog.dto.PostCreateDTO;
import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.dto.PostUpdateDTO;
import io.hexlet.blog.model.Post;

@Mapper(
    uses = { JsonNullableMapper.class, ReferenceMapper.class, TagMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PostMapper {
    @Mapping(target = "name", source = "title")
    @Mapping(target = "tags", source = "tagIds") // MapStruct использует ReferenceMapper для элементов
    public abstract Post map(PostCreateDTO dto);
    @Mapping(target = "title", source = "name")
    @Mapping(target = "authorId", source = "author.id")
    public abstract PostDTO map(Post model);
    @Mapping(source = "title", target = "name")
    @Mapping(target = "tags", source = "tagIds")
    public abstract void update(PostUpdateDTO dto, @MappingTarget Post model);
    @Mapping(source = "title", target = "name")
    @Mapping(target = "tags", source = "tagIds")
    public abstract void update(PostPatchDTO dto, @MappingTarget Post model);
}
