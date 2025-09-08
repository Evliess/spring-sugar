package evliess.io.jpa;

import evliess.io.entity.AuditToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuditTokenRepository extends JpaRepository<AuditToken, Long> {

    @Query("SELECT DISTINCT a.user FROM AuditToken a")
    List<String> findUsersByToken();

    @Query("SELECT a FROM AuditToken a WHERE a.user = ?1")
    List<AuditToken> findByUser(String user);

    @Query("SELECT a FROM AuditToken a WHERE a.consumedAt >= ?1 and a.consumedAt <= ?2")
    List<AuditToken> findByTimeSpan(Long start, Long end);

    @Query("SELECT a FROM AuditToken a WHERE a.user = ?1 and a.type='llm' order by a.consumedAt desc limit 1")
    AuditToken findLatestTokenByUser(String user);

    @Modifying
    @Query("DELETE FROM AuditToken a WHERE a.consumedAt < ?1")
    @Transactional
    int deleteLessThanConsumedAt(Long consumedAt);

}
