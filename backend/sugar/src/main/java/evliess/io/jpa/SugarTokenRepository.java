package evliess.io.jpa;

import evliess.io.entity.SugarToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SugarTokenRepository extends JpaRepository<SugarToken, Long> {
}
