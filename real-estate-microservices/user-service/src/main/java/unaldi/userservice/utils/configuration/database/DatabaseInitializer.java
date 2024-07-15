package unaldi.userservice.utils.configuration.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import unaldi.userservice.entity.ERole;
import unaldi.userservice.entity.Role;
import unaldi.userservice.repository.RoleRepository;

import java.util.Optional;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public DatabaseInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        addRoleIfNotFound(ERole.ROLE_USER);
        addRoleIfNotFound(ERole.ROLE_MODERATOR);
        addRoleIfNotFound(ERole.ROLE_ADMIN);
    }

    private void addRoleIfNotFound(ERole roleName) {
        Optional<Role> roleOptional = roleRepository.findByName(roleName);

        if (roleOptional.isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
