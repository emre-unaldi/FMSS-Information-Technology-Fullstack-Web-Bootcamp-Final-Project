package unaldi.authservice.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import unaldi.authservice.entity.Token;
import unaldi.authservice.entity.User;
import unaldi.authservice.entity.dto.response.UserResponse;
import unaldi.authservice.utils.security.services.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 12.07.2024
 */
@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper( AuthMapper.class );

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "accessToken", source = "accessToken")
    UserResponse userDetailsToUserResponse(UserDetailsImpl userDetails, String accessToken);

    @AfterMapping
    default void setRoles(UserDetailsImpl userDetails, @MappingTarget UserResponse userResponse) {
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        userResponse.setRoles(roles);
    }

    @Mapping(target = "authorities", ignore = true)
    UserDetailsImpl userToUserDetailsImpl(User user);

    @AfterMapping
    default void setAuthorities(User user, @MappingTarget UserDetailsImpl userDetailsImpl) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        userDetailsImpl.setAuthorities(authorities);
    }

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "userId", source = "userId"),
            @Mapping(target = "accessToken", source = "accessToken")
    })
    Token mapToToken(Long userId, String accessToken);
}
