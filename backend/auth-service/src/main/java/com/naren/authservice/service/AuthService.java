package com.naren.authservice.service;

import com.naren.authservice.dto.*;

import java.util.List;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse refreshToken(String refreshToken);

    UserResponse getCurrentUser(String username);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserResponse request);

    void deleteUser(Long id);

    void assignRole(Long userId, Long roleId);

    void removeRole(Long userId, Long roleId);

    RoleResponse createRole(RoleCreateRequest request);

    List<RoleResponse> getAllRoles();

    void deleteRole(Long id);

    RoleResponse addPermissionToRole(Long roleId, String permissionName);

    void removePermissionFromRole(Long roleId, String permissionName);
}
