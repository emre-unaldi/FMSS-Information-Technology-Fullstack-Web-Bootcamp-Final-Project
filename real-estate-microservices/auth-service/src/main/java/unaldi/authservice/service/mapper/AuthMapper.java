package unaldi.authservice.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import unaldi.authservice.entity.User;
import unaldi.authservice.entity.dto.response.LoginResponse;
import unaldi.authservice.entity.dto.response.LogoutResponse;
import unaldi.authservice.entity.dto.response.RefreshTokenResponse;
import unaldi.authservice.entity.dto.response.UserResponse;
import unaldi.authservice.utils.result.SuccessDataResult;
import unaldi.authservice.utils.result.SuccessResult;
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
    UserResponse userDetailsToUserResponse(UserDetailsImpl userDetails);

    @AfterMapping
    default void setRoles(UserDetailsImpl userDetails, @MappingTarget UserResponse userResponse) {
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        userResponse.setRoles(roles);
    }

    LoginResponse mapToLoginResponse(SuccessDataResult<UserResponse> user, ResponseCookie jwtCookie, ResponseCookie jwtRefreshCookie);

    LogoutResponse mapToLogoutResponse(SuccessResult result, ResponseCookie jwtCookie, ResponseCookie jwtRefreshCookie);

    RefreshTokenResponse mapToRefreshTokenResponse(SuccessResult result, ResponseCookie jwtCookie);

    @Mapping(target = "authorities", ignore = true)
    UserDetailsImpl userToUserDetailsImpl(User user);

    @AfterMapping
    default void setAuthorities(User user, @MappingTarget UserDetailsImpl userDetailsImpl) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        userDetailsImpl.setAuthorities(authorities);
    }
}
