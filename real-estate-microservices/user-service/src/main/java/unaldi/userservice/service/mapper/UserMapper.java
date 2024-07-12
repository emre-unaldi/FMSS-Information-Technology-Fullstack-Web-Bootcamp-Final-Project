package unaldi.userservice.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import unaldi.userservice.entity.Role;
import unaldi.userservice.entity.User;
import unaldi.userservice.entity.dto.request.SignUpRequest;
import unaldi.userservice.entity.dto.response.UserResponse;

import java.util.List;
import java.util.Objects;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(source = "encodedPassword", target = "password")
    })
    User signUpRequestToUser(SignUpRequest signUpRequest, String encodedPassword);

    @Mapping(target = "roles", ignore = true)
    UserResponse userToUserResponse(User user);

    @IterableMapping(elementTargetType = UserResponse.class)
    List<UserResponse> usersToUserResponses(List<User> photos);

    @AfterMapping
    default void setRoles(User user, @MappingTarget UserResponse userResponse) {
        List<String> rolesAsStrings = user.getRoles().stream()
                .map(Role::getName)
                .map(Objects::toString)
                .toList();

        userResponse.setRoles(rolesAsStrings);
    }

}
