package io.privacy.evidenceplatform.user;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    public UserService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public AppUser registerUser(String email, String passwordHash, UserRole role) {
        String normalizedEmail = normalizeEmail(email);

        if(userRepository.existsByEmail(normalizedEmail)){
            throw new IllegalArgumentException("Email is already registered");
        }

        AppUser user = new AppUser(
                normalizedEmail,
                passwordHash,
                role,
                OffsetDateTime.now()
        );

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AppUser getByEmail(String email) {
        String normalizedEmail = normalizeEmail(email);

        return userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        return email.trim().toLowerCase();
    }
}
