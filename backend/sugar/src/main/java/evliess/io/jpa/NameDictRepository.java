package evliess.io.jpa;

import evliess.io.entity.NameDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NameDictRepository extends JpaRepository<NameDict, Long> {
    @Modifying
    @Query("UPDATE NameDict a set a.meaning =?2 WHERE a.name = ?1 and a.type = ?3")
    @Transactional
    void updateByName(String name, String meaning, String type);

    @Query("SELECT a FROM NameDict a WHERE a.name = ?1 and a.type = ?2")
    NameDict findByName(String name, String type);

    @Query("SELECT a FROM NameDict a WHERE a.name LIKE %?1% and a.type = ?2")
    List<NameDict> findManyByName(String name, String type);

    @Query("SELECT a FROM NameDict a WHERE a.type = ?1")
    List<NameDict> findAllByType(String type);
}
