package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.TagCreateDTO;
import io.hexlet.blog.dto.TagDTO;
import io.hexlet.blog.dto.TagPatchDTO;
import io.hexlet.blog.dto.TagUpdateDTO;
import io.hexlet.blog.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(
    uses = { JsonNullableMapper.class, ReferenceMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TagMapper {
    public abstract Tag map(TagCreateDTO dto);
    public abstract TagDTO map(Tag model);
    public abstract void update(TagUpdateDTO dto, @MappingTarget Tag model);
    public abstract void update(TagPatchDTO dto, @MappingTarget Tag model);
    public abstract Set<Tag> toTagSet(Set<Long> tagIds);
    public abstract Set<Long> toIdSet(Set<Tag> tags);
    public Long toId(Tag tag) {
        return tag == null ? null : tag.getId();
    }
}
