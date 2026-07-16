package com.naren.authservice.config;

import com.naren.authservice.entity.Permission;
import com.naren.authservice.entity.Role;
import com.naren.authservice.repository.PermissionRepository;
import com.naren.authservice.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;

    public DataInitializer(RoleRepository roleRepo, PermissionRepository permissionRepo) {
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepo.count() > 0) return;

        Permission readEmployee = createPermissionIfNotExists("READ_EMPLOYEE");
        Permission writeEmployee = createPermissionIfNotExists("WRITE_EMPLOYEE");
        Permission readDepartment = createPermissionIfNotExists("READ_DEPARTMENT");
        Permission writeDepartment = createPermissionIfNotExists("WRITE_DEPARTMENT");
        Permission readLeave = createPermissionIfNotExists("READ_LEAVE");
        Permission writeLeave = createPermissionIfNotExists("WRITE_LEAVE");
        Permission manageUsers = createPermissionIfNotExists("MANAGE_USERS");
        Permission manageRoles = createPermissionIfNotExists("MANAGE_ROLES");

        roleRepo.save(Role.builder()
                .name("USER")
                .permissions(Set.of(readEmployee, readDepartment, readLeave))
                .build());

        roleRepo.save(Role.builder()
                .name("ADMIN")
                .permissions(Set.of(
                        readEmployee, writeEmployee,
                        readDepartment, writeDepartment,
                        readLeave, writeLeave,
                        manageUsers, manageRoles
                ))
                .build());

        logger.info("Seeded default roles: USER, ADMIN");
    }

    private Permission createPermissionIfNotExists(String name) {
        return permissionRepo.findByName(name)
                .orElseGet(() -> permissionRepo.save(
                        Permission.builder().name(name).build()
                ));
    }
}
