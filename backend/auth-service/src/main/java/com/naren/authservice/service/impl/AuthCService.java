package com.naren.authservice.service.impl;

import com.naren.authservice.dto.*;
import com.naren.authservice.dto.mapper.UserMapper;
import com.naren.authservice.entity.Permission;
import com.naren.authservice.entity.Role;
import com.naren.authservice.entity.UserAccount;
import com.naren.authservice.exception.ConflictException;
import com.naren.authservice.exception.ResourceNotFoundException;
import com.naren.authservice.repository.PermissionRepository;
import com.naren.authservice.repository.RoleRepository;
import com.naren.authservice.repository.UserAccountRepository;
import com.naren.authservice.security.JwtTokenProvider;
import com.naren.authservice.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("authCrudService")
@Transactional
public class AuthCService implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthCService.class);

    private final UserAccountRepository userRepo;
    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    public AuthCService(
            UserAccountRepository userRepo,
            RoleRepository roleRepo,
            PermissionRepository permissionRepo,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider,
            UserMapper userMapper
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        String username = sanitize(request.username());
        String email = sanitize(request.email());
        String firstName = sanitize(request.firstName());
        String lastName = sanitize(request.lastName());

        if (userRepo.existsByUsername(username)) {
            throw new ConflictException("Username already taken: " + username);
        }
        if (userRepo.existsByEmail(email)) {
            throw new ConflictException("Email already taken: " + email);
        }

        UserAccount account = UserAccount.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .firstName(firstName)
                .lastName(lastName)
                .enabled(true)
                .locked(false)
                .build();

        Role userRole = roleRepo.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = Role.builder().name("USER").build();
                    return roleRepo.save(newRole);
                });
        account.getRoles().add(userRole);

        UserAccount saved;
        try {
            saved = userRepo.save(account);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to register user: {}", e.getMessage());
            throw new ConflictException("Username or email already taken");
        }
        return userMapper.apply(saved);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        UserAccount account = userRepo.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new ResourceNotFoundException("Invalid username or password");
        }

        if (!account.getEnabled()) {
            throw new ResourceNotFoundException("Account is disabled");
        }

        if (account.getLocked()) {
            throw new ResourceNotFoundException("Account is locked");
        }

        List<String> roles = account.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken = tokenProvider.generateAccessToken(account.getUsername(), roles);
        String refreshToken = tokenProvider.generateRefreshToken(account.getUsername());

        return new TokenResponse(
                accessToken,
                refreshToken,
                "Bearer",
                tokenProvider.getAccessExpiresInSeconds(),
                tokenProvider.getExpiresAt(accessToken)
        );
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        String username = tokenProvider.getUsernameFromToken(refreshToken);

        UserAccount account = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<String> roles = account.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String newAccessToken = tokenProvider.generateAccessToken(username, roles);
        String newRefreshToken = tokenProvider.generateRefreshToken(username);

        return new TokenResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                tokenProvider.getAccessExpiresInSeconds(),
                tokenProvider.getExpiresAt(newAccessToken)
        );
    }

    @Override
    public UserResponse getCurrentUser(String username) {
        throw new UnsupportedOperationException("Use authQueryService for query operations");
    }

    @Override
    public UserResponse getUserById(Long id) {
        throw new UnsupportedOperationException("Use authQueryService for query operations");
    }

    @Override
    public List<UserResponse> getAllUsers() {
        throw new UnsupportedOperationException("Use authQueryService for query operations");
    }

    @Override
    public UserResponse updateUser(Long id, UserResponse request) {
        UserAccount existing = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (request.firstName() != null) existing.setFirstName(request.firstName());
        if (request.lastName() != null) existing.setLastName(request.lastName());
        if (request.email() != null) existing.setEmail(request.email());
        if (request.enabled() != null) existing.setEnabled(request.enabled());
        if (request.locked() != null) existing.setLocked(request.locked());

        try {
            userRepo.save(existing);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to update user {}: {}", id, e.getMessage());
            throw new ConflictException("Email already taken");
        }
        return userMapper.apply(existing);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }

    @Override
    public void assignRole(Long userId, Long roleId) {
        UserAccount user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        user.getRoles().add(role);
        userRepo.save(user);
    }

    @Override
    public void removeRole(Long userId, Long roleId) {
        UserAccount user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        user.getRoles().remove(role);
        userRepo.save(user);
    }

    @Override
    public RoleResponse createRole(RoleCreateRequest request) {
        String roleName = sanitize(request.name());

        if (roleRepo.existsByName(roleName)) {
            throw new ConflictException("Role already exists: " + roleName);
        }

        Role role = Role.builder().name(roleName).build();

        Role saved;
        try {
            saved = roleRepo.save(role);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to create role: {}", e.getMessage());
            throw new ConflictException("Role already exists: " + roleName);
        }

        return new RoleResponse(saved.getId(), saved.getName(), new HashSet<>());
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        throw new UnsupportedOperationException("Use authQueryService for query operations");
    }

    @Override
    public void deleteRole(Long id) {
        if (!roleRepo.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepo.deleteById(id);
    }

    @Override
    public RoleResponse addPermissionToRole(Long roleId, String permissionName) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        Permission permission = permissionRepo.findByName(permissionName)
                .orElseGet(() -> {
                    Permission newPerm = Permission.builder().name(permissionName).build();
                    return permissionRepo.save(newPerm);
                });

        role.getPermissions().add(permission);
        Role saved = roleRepo.save(role);

        return new RoleResponse(
                saved.getId(),
                saved.getName(),
                saved.getPermissions().stream()
                        .map(Permission::getName)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public void removePermissionFromRole(Long roleId, String permissionName) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

        Permission permission = permissionRepo.findByName(permissionName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permission not found: " + permissionName
                ));

        role.getPermissions().remove(permission);
        roleRepo.save(role);
    }

    private String sanitize(String value) {
        if (value == null) return null;
        return value.trim().isEmpty() ? null : value.trim();
    }
}
