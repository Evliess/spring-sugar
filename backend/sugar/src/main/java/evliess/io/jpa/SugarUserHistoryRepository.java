package evliess.io.jpa;

import evliess.io.entity.SugarUserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SugarUserHistoryRepository extends JpaRepository<SugarUserHistory, Long> {

    @Query("SELECT a FROM SugarUserHistory a WHERE a.username = ?1 and a.message = ?2")
    SugarUserHistory findByNameAndMsg(String username, String message);

}
