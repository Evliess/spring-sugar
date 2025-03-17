package evliess.io.jpa;

import evliess.io.entity.AuditToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuditTokenRepository extends JpaRepository<AuditToken, Long> {

    @Query("SELECT DISTINCT a.user FROM AuditToken a")
    List<String> findUsersByToken();

    @Query("SELECT a FROM AuditToken a WHERE a.user = ?1")
    List<AuditToken> findByUser(String user);

    @Query("SELECT a FROM AuditToken a WHERE a.consumedAt >= ?1 and a.consumedAt <= ?2")
    List<AuditToken> findByTimeSpan(Long start, Long end);
}
