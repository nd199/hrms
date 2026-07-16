package com.naren.authservice.dto.mapper;

import com.naren.authservice.dto.RoleResponse;
import com.naren.authservice.entity.Role;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoleMapper implements Function<Role, RoleResponse> {

    @Override
    public RoleResponse apply(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getPermissions().stream()
                        .map(permission -> permission.getName())
                        .collect(Collectors.toSet())
        );
    }
}
