package evliess.io.service;

import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import evliess.io.config.Constants;
import evliess.io.entity.AuditToken;
import evliess.io.entity.OrderStatus;
import evliess.io.entity.WOrder;
import evliess.io.jpa.AuditTokenRepository;
import evliess.io.jpa.OrderRepository;
import evliess.io.jpa.SugarUserRepository;
import evliess.io.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final WepayService wepayService;
    private final ThreadPoolTaskExecutor paymentExecutor;
    private final OrderRepository orderRepository;
    private final AuditTokenRepository auditTokenRepository;
    private final SugarUserRepository sugarUserRepository;

    @Autowired
    public OrderService(SugarUserRepository sugarUserRepository, AuditTokenRepository auditTokenRepository, OrderRepository orderRepository, WepayService wepayService, @Qualifier("paymentAsyncExecutor") ThreadPoolTaskExecutor paymentExecutor) {
        this.wepayService = wepayService;
        this.paymentExecutor = paymentExecutor;
        this.orderRepository = orderRepository;
        this.auditTokenRepository = auditTokenRepository;
        this.sugarUserRepository = sugarUserRepository;
    }

    /**
     * 处理支付回调，保证幂等性
     *
     * @param outTradeNo    商户订单号
     * @param transactionId 微信支付订单号
     * @param totalAmount   支付金额（分）
     * @param payerOpenId   支付者openid
     * @return true-首次处理成功，false-重复通知
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean processPaymentCallback(String outTradeNo,
                                          String transactionId,
                                          Integer totalAmount,
                                          String payerOpenId) {
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
        updateOrderAfterPayment(outTradeNo, transactionId, payerOpenId);

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
                                         String openId) {
        this.updateOrderByOutTradeNo(outTradeNo, transactionId, openId);
        log.info("Update Order {} to Paid status, transactionId：{}", outTradeNo, transactionId);
    }

    /**
     * 执行支付后的业务逻辑
     * 通过线程池处理
     */
    private void executePostPaymentBusiness(WOrder order) {
        log.info("Generate token for OutTradeNo: {} ", order.getOutTradeNo());
        String credentials = TokenUtils.generateToken(
                sugarUserRepository.findByUsername(SugarUserService.S_DAYS)
                        .getAccessKey());
        AuditToken auditToken = new AuditToken(order.getOpenId(), credentials, Constants.TYPE_LLM);
        this.auditTokenRepository.save(auditToken);

    }

    private WOrder findOrderByOutTradeNo(String outTradeNo) {
        return orderRepository.findByOutTradeNo(outTradeNo);
    }

    public Map<String, String> createJsapiOrder(String openid, String amount) {
        if (openid == null || openid.isEmpty()) {
            throw new RuntimeException("openid is empty!");
        }
        if (amount == null || amount.isEmpty()) {
            throw new RuntimeException("amount is empty!");
        }
        String outTradeNo = generateOutTradeNo();
        log.info("Creating preOrder with openid: {}, amount: {}", openid, amount);
        PrepayWithRequestPaymentResponse resp = wepayService.createWxPrepay(outTradeNo, openid, Integer.parseInt(amount));
        Map<String, String> respMap = wepayService.buildPayParams(resp);
        log.info("Succeed Created preOrder with prepay_id: {}, nonceStr: {}", respMap.get("package"), respMap.get("nonceStr"));
        this.saveUnpaidOrder(outTradeNo, Integer.parseInt(amount), openid);
        return respMap;
    }

    private void saveUnpaidOrder(String outTradeNo, Integer totalAmount, String openId) {
        WOrder order = new WOrder(outTradeNo, OrderStatus.UNPAID, totalAmount, openId);
        orderRepository.save(order);
    }

    private void updateOrderByOutTradeNo(String outTradeNo, String transactionId,
                                         String openId) {
        WOrder order = this.orderRepository.findByOutTradeNo(outTradeNo);
        order.setStatus(OrderStatus.PAID);
        order.setTransactionId(transactionId);
        order.setUpdatedAt(Instant.now().toEpochMilli());
        this.orderRepository.save(order);
    }

    private String generateOutTradeNo() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "WORDER" + timestamp + random;
    }

}
