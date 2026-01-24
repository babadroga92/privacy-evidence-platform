package io.privacy.evidenceplatform.user;


import io.privacy.evidenceplatform.auth.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser registerUser(String email, String passwordHash, UserRole role) {
        String normalizedEmail = normalizeEmail(email);

        if(userRepository.existsByEmail(normalizedEmail)){
            throw new EmailAlreadyRegisteredException(normalizedEmail);
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
                .orElseThrow(() -> new UserNotFoundException(normalizedEmail));
    }

    @Transactional(readOnly = true)
    public AppUser authenticate(String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);

        AppUser user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(InvalidCredentialsException::new);

        boolean matches = passwordEncoder.matches(rawPassword, user.getPasswordHash());
        if(!matches){
            throw new InvalidCredentialsException();
        }

        return user;
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        return email.trim().toLowerCase();
    }
}
