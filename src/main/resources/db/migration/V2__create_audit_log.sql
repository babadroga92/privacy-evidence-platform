CREATE TABLE audit_log (
  id BIGSERIAL PRIMARY KEY,
  action VARCHAR(100) NOT NULL,
  success BOOLEAN NOT NULL,

  user_id BIGINT NULL,
  actor_email VARCHAR(255) NULL,

  ip_address VARCHAR(100) NULL,
  user_agent TEXT NULL,

  metadata TEXT NULL,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_log_created_at ON audit_log(created_at);
CREATE INDEX idx_audit_log_action ON audit_log(action);
CREATE INDEX idx_audit_log_user_id ON audit_log(user_id);
