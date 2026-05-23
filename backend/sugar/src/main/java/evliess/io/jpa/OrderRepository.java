package evliess.io.jpa;

import evliess.io.entity.WOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<WOrder, Long> {
    @Query("SELECT a FROM WOrder a WHERE a.openId = ?1")
    List<WOrder> findByOpenId(String openId);

    @Query("SELECT a FROM WOrder a WHERE a.outTradeNo = ?1")
    WOrder findByOutTradeNo(String outTradeNo);
}
