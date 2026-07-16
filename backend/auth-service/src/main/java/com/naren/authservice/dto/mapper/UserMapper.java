package com.naren.authservice.dto.mapper;

import com.naren.authservice.dto.UserResponse;
import com.naren.authservice.entity.UserAccount;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserMapper implements Function<UserAccount, UserResponse> {

    @Override
    public UserResponse apply(UserAccount user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getEnabled(),
                user.getLocked(),
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
