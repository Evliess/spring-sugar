package evliess.io.controller;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.model.Transaction;
import evliess.io.config.Constants;
import evliess.io.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class WxPayController {

    private static final Logger log = LoggerFactory.getLogger(WxPayController.class);
    private final Config wxPayConfig;

    private final OrderService orderService;

    @Autowired
    public WxPayController(OrderService orderService, Config wxPayConfig) {
        this.orderService = orderService;
        this.wxPayConfig = wxPayConfig;
    }

    @PostMapping("/public/order/create")
    public ResponseEntity<String> createOrder(@RequestBody String body) {
        JSONObject jsonNode = JSON.parseObject(body);
        String openId = jsonNode.getString(Constants.X_OPENID);
        String amount = jsonNode.getString("amount");
        Map<String, String> resp = this.orderService.createJsapiOrder(openId, amount);
        JSONObject jsonObject = new JSONObject(resp);
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/notify")
    public ResponseEntity<String> callbackPayment(HttpServletRequest request) {
        try {
            // 1. 获取原始报文和请求头
            String body = IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);

            String wechatPaySerial = request.getHeader("Wechatpay-Serial");
            String wechatpayNonce = request.getHeader("Wechatpay-Nonce");
            String wechatSignature = request.getHeader("Wechatpay-Signature");
            String wechatTimestamp = request.getHeader("Wechatpay-Timestamp");

            // 2. 构建验签参数
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(wechatPaySerial)
                    .nonce(wechatpayNonce)
                    .signature(wechatSignature)
                    .timestamp(wechatTimestamp)
                    .body(body)
                    .build();

            // 3. 初始化解析器（关键点：直接使用共用的 Config）
            NotificationParser parser;
            if (wxPayConfig instanceof NotificationConfig) {
                // 大多数官方 Config（如 RSAAutoCertificateConfig）都实现了此接口
                parser = new NotificationParser((NotificationConfig) wxPayConfig);
            } else {
                log.error("当前的 Config 类型 {} 未实现 NotificationConfig 接口", wxPayConfig.getClass().getName());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(createErrorResponse("服务器配置错误"));
            }

            // 4. 验签、解密并转换为业务对象
            Transaction transaction = parser.parse(requestParam, Transaction.class);

            // 5. 你的业务逻辑（只处理支付成功）
            if ("SUCCESS".equals(transaction.getTradeState().name())) {
                boolean success = orderService.processPaymentCallback(
                        transaction.getOutTradeNo(),
                        transaction.getTransactionId(),
                        transaction.getAmount().getTotal(),
                        transaction.getPayer().getOpenid()
                );

                if (!success) {
                    log.error("订单 {} 业务处理失败", transaction.getOutTradeNo());
                    // 按官方指引，业务处理失败应返回 500
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(createErrorResponse("业务处理失败"));
                }
            }

            // 6. 成功应答
            return ResponseEntity.ok(createSuccessResponse());

        } catch (Exception e) {
            log.error("支付回调处理异常", e);
            // 验签失败（ValidationException）或其他异常，按官方建议返回 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("验签失败或系统错误"));
        }
    }

    // 辅助方法
    private String createSuccessResponse() {
        JSONObject json = new JSONObject();
        json.put("code", "SUCCESS");
        json.put("message", "成功");
        return json.toString();
    }

    private String createErrorResponse(String message) {
        JSONObject json = new JSONObject();
        json.put("code", "FAIL");
        json.put("message", message);
        return json.toString();
    }


}
