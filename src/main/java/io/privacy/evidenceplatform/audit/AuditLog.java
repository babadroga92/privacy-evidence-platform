package io.privacy.evidenceplatform.audit;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "actor_email", length = 255)
    private String actorEmail;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected AuditLog() {}

    public AuditLog(String action, boolean success, Long userId, String actorEmail,
                    String ipAddress, String userAgent, String metadata, OffsetDateTime createdAt) {
        this.action = action;
        this.success = success;
        this.userId = userId;
        this.actorEmail = actorEmail;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.metadata = metadata;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getAction() { return action; }
    public boolean isSuccess() { return success; }
    public Long getUserId() { return userId; }
    public String getActorEmail() { return actorEmail; }
    public String getIpAddress() { return ipAddress; }
    public String getUserAgent() { return userAgent; }
    public String getMetadata() { return metadata; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
