package io.hexlet.blog.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.BeforeMapping;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Creation: standard behavior (null -> null)
    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO dto);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO dto, @MappingTarget User user) {
        String password = dto.getPassword();
        if (password != null) {
            user.setPasswordDigest(passwordEncoder.encode(password));
        }
    }

    @BeforeMapping
    public void encryptPassword(UserUpdateDTO dto, @MappingTarget User user) {
        String password = dto.getPassword();
        if (password != null) {
            user.setPasswordDigest(passwordEncoder.encode(password));
        }
    }

    @BeforeMapping
    public void encryptPassword(UserPatchDTO dto, @MappingTarget User user) {
        JsonNullable<String> password = dto.getPassword();
        if (password != null && password.isPresent()) {
            user.setPasswordDigest(passwordEncoder.encode(password.get()));
        }
    }

    public abstract UserDTO map(User model);

    // PUT: Full update.
    // By default, SET_TO_NULL is used, so a null from the DTO will overwrite the data in the database.
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    // PATCH: Partial update.
    // Explicitly specify IGNORE so that null fields in the DTO don't affect the entity.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void update(UserPatchDTO dto, @MappingTarget User model);
}
