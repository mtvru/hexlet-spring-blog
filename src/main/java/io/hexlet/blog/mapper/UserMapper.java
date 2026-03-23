package io.hexlet.blog.mapper;

import io.hexlet.blog.dto.UserPatchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import io.hexlet.blog.dto.UserCreateDTO;
import io.hexlet.blog.dto.UserDTO;
import io.hexlet.blog.dto.UserUpdateDTO;
import io.hexlet.blog.model.User;

@Mapper(
    uses = {JsonNullableMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract User map(UserCreateDTO dto);

    public abstract UserDTO map(User model);

    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    public abstract void update(UserPatchDTO dto, @MappingTarget User model);
}
