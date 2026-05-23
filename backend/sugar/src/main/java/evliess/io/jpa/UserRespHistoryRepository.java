package evliess.io.jpa;

import evliess.io.entity.UserRespHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRespHistoryRepository extends JpaRepository<UserRespHistory, Long> {
    @Query("SELECT a FROM UserRespHistory a WHERE a.username = ?1 order by a.consumedAt desc")
    List<UserRespHistory> findUserRespHistoryByUsername(String username);
}
