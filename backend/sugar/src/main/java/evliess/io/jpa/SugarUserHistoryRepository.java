package evliess.io.jpa;

import evliess.io.entity.SugarUserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SugarUserHistoryRepository extends JpaRepository<SugarUserHistory, Long> {

    @Query("SELECT a FROM SugarUserHistory a WHERE a.username = ?1 and a.message = ?2")
    SugarUserHistory findByNameAndMsg(String username, String message);

    @Modifying
    @Query("DELETE FROM SugarUserHistory a WHERE a.consumedAt < ?1")
    @Transactional
    int deleteLessThanConsumedAt(Long consumedAt);

}
