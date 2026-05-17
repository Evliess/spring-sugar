package evliess.io.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "WOrder")
public class WOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "outTradeNo")
    private String outTradeNo;
    @Column(name = "orderStatus")
    private OrderStatus status;
    @Column(name = "totalAmount")
    private Integer totalAmount;

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
}
