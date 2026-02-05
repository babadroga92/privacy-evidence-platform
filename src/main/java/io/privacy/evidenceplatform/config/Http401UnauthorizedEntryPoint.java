package io.privacy.evidenceplatform.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.privacy.evidenceplatform.api.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.OffsetDateTime;

/**
 * When an unauthenticated request hits a protected endpoint, Spring Security
 * calls this instead of returning an empty 403. Returns 401 with a JSON body
 * in the same format as other API errors.
 */
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public Http401UnauthorizedEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError error = new ApiError(
                OffsetDateTime.now(),
                401,
                "Unauthorized",
                "Not authenticated",
                request.getRequestURI()
        );
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
