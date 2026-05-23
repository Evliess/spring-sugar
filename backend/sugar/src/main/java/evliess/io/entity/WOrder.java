package evliess.io.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "WOrder")
public class WOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "out_trade_no")
    private String outTradeNo;
    @Column(name = "order_status")
    private OrderStatus status;
    @Column(name = "total_amount")
    private Integer totalAmount;
    @Column(name = "open_id")
    private String openId;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "updated_at")
    private Long updatedAt;

    public WOrder() {
    }

    public WOrder(String outTradeNo, OrderStatus status, Integer totalAmount, String openId) {
        this.outTradeNo = outTradeNo;
        this.status = status;
        this.totalAmount = totalAmount;
        this.openId = openId;
        this.updatedAt = Instant.now().toEpochMilli();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
