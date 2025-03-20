package evliess.io.jpa;

import evliess.io.entity.SugarToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SugarTokenRepository extends JpaRepository<SugarToken, Long> {
    @Query("SELECT a FROM SugarToken a WHERE a.token = ?1")
    SugarToken findByToken(String token);
}
