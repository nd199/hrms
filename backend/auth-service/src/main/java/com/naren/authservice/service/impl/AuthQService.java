package com.naren.authservice.service.impl;

import com.naren.authservice.dto.*;
import com.naren.authservice.dto.mapper.RoleMapper;
import com.naren.authservice.dto.mapper.UserMapper;
import com.naren.authservice.entity.UserAccount;
import com.naren.authservice.exception.ResourceNotFoundException;
import com.naren.authservice.repository.RoleRepository;
import com.naren.authservice.repository.UserAccountRepository;
import com.naren.authservice.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("authQueryService")
@Transactional(readOnly = true)
public class AuthQService implements AuthService {

    private final UserAccountRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public AuthQService(
            UserAccountRepository userRepo,
            RoleRepository roleRepo,
            UserMapper userMapper,
            RoleMapper roleMapper
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public UserResponse getCurrentUser(String username) {
        UserAccount account = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return userMapper.apply(account);
    }

    @Override
    public UserResponse getUserById(Long id) {
        UserAccount account = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.apply(account);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepo.findAll().stream()
                .map(roleMapper)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        throw new UnsupportedOperationException("Use authCrudService for create operations");
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Use authCrudService for create operations");
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        throw new UnsupportedOperationException("Use authCrudService for create operations");
    }

    @Override
    public UserResponse updateUser(Long id, UserResponse request) {
        throw new UnsupportedOperationException("Use authCrudService for update operations");
    }

    @Override
    public void deleteUser(Long id) {
        throw new UnsupportedOperationException("Use authCrudService for delete operations");
    }

    @Override
    public void assignRole(Long userId, Long roleId) {
        throw new UnsupportedOperationException("Use authCrudService for update operations");
    }

    @Override
    public void removeRole(Long userId, Long roleId) {
        throw new UnsupportedOperationException("Use authCrudService for update operations");
    }

    @Override
    public RoleResponse createRole(RoleCreateRequest request) {
        throw new UnsupportedOperationException("Use authCrudService for create operations");
    }

    @Override
    public void deleteRole(Long id) {
        throw new UnsupportedOperationException("Use authCrudService for delete operations");
    }

    @Override
    public RoleResponse addPermissionToRole(Long roleId, String permissionName) {
        throw new UnsupportedOperationException("Use authCrudService for update operations");
    }

    @Override
    public void removePermissionFromRole(Long roleId, String permissionName) {
        throw new UnsupportedOperationException("Use authCrudService for update operations");
    }
}
