package io.hexlet.blog.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import io.hexlet.blog.dto.UserPatchDTO;
import io.hexlet.blog.dto.UserCreateDTO;
import io.hexlet.blog.dto.UserDTO;
import io.hexlet.blog.dto.UserUpdateDTO;
import io.hexlet.blog.model.User;

@Mapper(
    uses = {JsonNullableMapper.class},
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    // Creation: standard behavior (null -> null)
    public abstract User map(UserCreateDTO dto);

    public abstract UserDTO map(User model);

    // PUT: Full update.
    // By default, SET_TO_NULL is used, so a null from the DTO will overwrite the data in the database.
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    // PATCH: Partial update.
    // Explicitly specify IGNORE so that null fields in the DTO don't affect the entity.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void update(UserPatchDTO dto, @MappingTarget User model);
}
