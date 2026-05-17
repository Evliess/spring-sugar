package evliess.io.service;

import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import evliess.io.entity.OrderStatus;
import evliess.io.entity.WOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final WepayService wepayService;
    private final ThreadPoolTaskExecutor paymentExecutor;

    @Autowired
    public OrderService(WepayService wepayService, @Qualifier("paymentAsyncExecutor") ThreadPoolTaskExecutor paymentExecutor) {
        this.wepayService = wepayService;
        this.paymentExecutor = paymentExecutor;
    }

    /**
     * 处理支付回调，保证幂等性
     *
     * @param outTradeNo    商户订单号
     * @param transactionId 微信支付订单号
     * @param totalAmount   支付金额（分）
     * @param payerOpenid   支付者openid
     * @return true-首次处理成功，false-重复通知
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean processPaymentCallback(String outTradeNo,
                                          String transactionId,
                                          Integer totalAmount,
                                          String payerOpenid) {
        // 1. 根据商户订单号查询订单
        WOrder order = findOrderByOutTradeNo(outTradeNo);

        if (order == null) {
            log.error("Order not exist：{}", outTradeNo);
            return true;
        }

        // 2. 幂等性检查：如果订单已经是支付成功状态，直接返回false
        if (order.getStatus() == OrderStatus.PAID) {
            log.info("Order {} is paid", outTradeNo);
            return false;
        }

        // 3. 校验支付金额是否一致（重要！防止金额被篡改）
        if (!order.getTotalAmount().equals(totalAmount)) {
            log.error("Order amount {} verify failed！Must be ：{}，but user paid is：{}",
                    outTradeNo, order.getTotalAmount(), totalAmount);
            return true;
        }

        // 4. 更新订单状态
        updateOrderAfterPayment(outTradeNo, transactionId, payerOpenid);

        // 5. 这里可以异步执行后续业务逻辑
        // 例如：发送消息队列、记录支付日志、发放会员权益等
        paymentExecutor.execute(() -> {
            executePostPaymentBusiness(order);
        });
        return true;
    }

    /**
     * 更新订单支付成功信息
     */
    private void updateOrderAfterPayment(String outTradeNo,
                                         String transactionId,
                                         String payerOpenid) {
        // 数据库更新操作
        // orderMapper.updatePaymentSuccess(outTradeNo, transactionId, payerOpenid, LocalDateTime.now());
        log.info("Update Order {} to Paid status, transactionId：{}", outTradeNo, transactionId);
    }

    /**
     * 执行支付后的业务逻辑
     * 推荐使用异步方式处理，例如 @Async 或消息队列
     */
    private void executePostPaymentBusiness(WOrder order) {
        // 这里建议用异步方式执行，避免阻塞微信回调响应
        log.info("开始处理订单 {} 的后续业务逻辑...", order.getOutTradeNo());

        // 示例业务：
        // 1. 发送支付成功通知给用户
        // 2. 更新库存
        // 3. 发放积分/优惠券
        // 4. 记录支付流水
    }

    // 以下为示例方法，实际需要连接数据库
    private WOrder findOrderByOutTradeNo(String outTradeNo) {
        // 模拟从数据库查询
        WOrder order = new WOrder();
        order.setOutTradeNo(outTradeNo);
        order.setStatus(OrderStatus.UNPAID);
        order.setTotalAmount(100); // 1元 = 100分
        return order;
    }

    public Map<String, String> createJsapiOrder(String openid, String amount) {
        if (openid == null || openid.isEmpty()) {
            throw new RuntimeException("openid is empty!");
        }
        if (amount == null || amount.isEmpty()) {
            throw new RuntimeException("amount is empty!");
        }
        PrepayWithRequestPaymentResponse resp = wepayService.createWxPrepay(openid, Integer.parseInt(amount));
        return wepayService.buildPayParams(resp);
    }

}
