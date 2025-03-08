package evliess.io.jpa;

import evliess.io.entity.SugarUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SugarUserRepository extends JpaRepository<SugarUser, Long> {
    SugarUser findByUsername(String username);
}
