package com.gaipov.talim_crm.config;

import com.gaipov.talim_crm.entity.AuthEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import com.gaipov.talim_crm.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        final String username = "superadmin";
        final String defaultPassword = "super123"; // change after first login

        profileRepository.findByUsername(username).ifPresentOrElse(existing -> {
            // Ensure role and status are correct
            boolean changed = false;
            if (existing.getRoles() != UserRole.ADMIN) {
                existing.setRoles(UserRole.ADMIN);
                changed = true;
            }
            if (existing.getStatus() != UserStatus.ACTIVE) {
                existing.setStatus(UserStatus.ACTIVE);
                changed = true;
            }
            if (existing.getPassword() == null || existing.getPassword().length() < 20) {
                // If no password or looks like plain text, reset to default encoded
                existing.setPassword(passwordEncoder.encode(defaultPassword));
                changed = true;
            }
            if (changed) {
                profileRepository.save(existing);
                log.info("Updated existing SUPER_ADMIN '{}'.", username);
            } else {
                log.info("SUPER_ADMIN '{}' already exists.", username);
            }
        }, () -> {
            AuthEntity admin = new AuthEntity();
            admin.setFullName("Super Admin");
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            admin.setPhoneNum("+");
            admin.setRoles(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            admin.setCreated_at(new Date());
            profileRepository.save(admin);
            log.info("Created default SUPER_ADMIN user '{}' (please change password).", username);
        });
    }
}
