package com.naren.authservice.controller;

import com.naren.authservice.dto.*;
import com.naren.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Qualifier("authQueryService")
    private final AuthService authQueryService;

    @Qualifier("authCrudService")
    private final AuthService authCrudService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authCrudService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authCrudService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        TokenResponse response = authCrudService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        UserResponse response = authQueryService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> response = authQueryService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = authQueryService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserResponse request
    ) {
        UserResponse response = authCrudService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authCrudService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> assignRole(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        authCrudService.assignRole(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRole(
            @PathVariable Long userId,
            @PathVariable Long roleId
    ) {
        authCrudService.removeRole(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleCreateRequest request) {
        RoleResponse response = authCrudService.createRole(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> response = authQueryService.getAllRoles();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        authCrudService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/roles/{roleId}/permissions/{permissionName}")
    public ResponseEntity<RoleResponse> addPermissionToRole(
            @PathVariable Long roleId,
            @PathVariable String permissionName
    ) {
        RoleResponse response = authCrudService.addPermissionToRole(roleId, permissionName);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/roles/{roleId}/permissions/{permissionName}")
    public ResponseEntity<Void> removePermissionFromRole(
            @PathVariable Long roleId,
            @PathVariable String permissionName
    ) {
        authCrudService.removePermissionFromRole(roleId, permissionName);
        return ResponseEntity.noContent().build();
    }
}
