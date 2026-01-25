package io.privacy.evidenceplatform.audit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class AuditService {

    public static final String ACTION_LOGIN = "AUTH_LOGIN";

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void logLoginAttempt(Long userId, String email, boolean success, String ip, String userAgent) {
        AuditLog log = new AuditLog(
                ACTION_LOGIN,
                success,
                userId,
                email,
                ip,
                userAgent,
                null,
                OffsetDateTime.now()
        );
        auditLogRepository.save(log);
    }
}
