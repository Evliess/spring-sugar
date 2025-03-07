package evliess.io.jpa;

import evliess.io.entity.AuditToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditTokenRepository extends JpaRepository<AuditToken, Long> {
}
