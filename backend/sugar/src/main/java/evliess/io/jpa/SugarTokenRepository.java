package evliess.io.jpa;

import evliess.io.entity.SugarToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SugarTokenRepository extends JpaRepository<SugarToken, Long> {
    @Query("SELECT a FROM SugarToken a WHERE a.token = ?1")
    SugarToken findByToken(String token);

    @Modifying
    @Query("UPDATE SugarToken a set a.token =?2 WHERE a.token = ?1")
    @Transactional
    void updateByToken(String oldToken, String newToken);
}
