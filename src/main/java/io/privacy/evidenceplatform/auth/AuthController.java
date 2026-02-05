package io.privacy.evidenceplatform.auth;

import io.privacy.evidenceplatform.audit.AuditService;
import io.privacy.evidenceplatform.user.AppUser;
import io.privacy.evidenceplatform.user.UserRole;
import io.privacy.evidenceplatform.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;
    private final JwtService jwtService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder,
                          AuditService auditService, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
        this.jwtService = jwtService;
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

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        try {
            AppUser user = userService.authenticate(request.getEmail(), request.getPassword());

            // success audit
            auditService.logLoginAttempt(user.getId(), user.getEmail(), true, ip, userAgent);

            String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getUserRole());
            return new LoginResponse(user.getId(), user.getEmail(), user.getUserRole().name(), token);

        } catch (InvalidCredentialsException ex) {
            auditService.logLoginAttempt(null, request.getEmail(), false, ip, userAgent);
            throw ex;
        }
    }

    @GetMapping("/me")
    public LoginResponse me(@AuthenticationPrincipal JwtPrincipal principal) {
        if (principal == null) {
            throw new InvalidCredentialsException("Not authenticated");
        }
        return new LoginResponse(
                principal.userId(),
                principal.email(),
                principal.role().name(),
                null  // Don't re-expose token on every /me call
        );
    }

    private UserRole parseRoleOrDefault(String rawRole) {
        if (rawRole == null || rawRole.isBlank()) {
            return UserRole.ENGINEER;
        }
        return UserRole.valueOf(rawRole.trim().toUpperCase());
    }
}
