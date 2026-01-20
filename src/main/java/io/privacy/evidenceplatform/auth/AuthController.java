package io.privacy.evidenceplatform.auth;


import io.privacy.evidenceplatform.user.AppUser;
import io.privacy.evidenceplatform.user.UserRole;
import io.privacy.evidenceplatform.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        String passwordHash = passwordEncoder.encode(request.getPassword());

        UserRole role = parseRoleOrDefault(request.getRole());

        AppUser created = userService.registerUser(
                request.getEmail(),
                passwordHash,
                role
        );

        return new RegisterResponse(
                created.getId(),
                created.getEmail(),
                created.getUserRole().name()
        );
    }

    private UserRole parseRoleOrDefault(String rawRole) {
        if (rawRole == null || rawRole.isBlank()) {
            return UserRole.ENGINEER;
        }
        return  UserRole.valueOf(rawRole.trim().toUpperCase());
    }
}
