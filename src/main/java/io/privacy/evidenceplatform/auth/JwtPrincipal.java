package io.privacy.evidenceplatform.auth;

import io.privacy.evidenceplatform.user.UserRole;

import java.util.Objects;

/**
 * Principal set in SecurityContext after successful JWT validation.
 * Used by protected endpoints (e.g. GET /auth/me) without hitting the DB.
 */
public record JwtPrincipal(Long userId, String email, UserRole role) {

    public JwtPrincipal {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(email);
        Objects.requireNonNull(role);
    }
}
