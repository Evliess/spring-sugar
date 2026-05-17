package evliess.io.service;

import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WepayService {

    private final JsapiServiceExtension jsapiService;

    @Value("${wxpay.appid}")
    private String appId;
    @Value("${wxpay.mchid}")
    private String mchId;
    @Value("${wxpay.notify-url}")
    private String notifyUrl;

    @Autowired
    public WepayService(JsapiServiceExtension jsapiService) {
        this.jsapiService = jsapiService;
    }

    public PrepayWithRequestPaymentResponse createWxPrepay(String openid, int amount) {
        PrepayRequest request = new PrepayRequest();
        request.setAppid(appId);
        request.setMchid(mchId);
        request.setOutTradeNo(generateOutTradeNo());
        request.setDescription("desc");
        request.setNotifyUrl(notifyUrl);

        Amount amountObj = new Amount();
        amountObj.setTotal(amount);
        amountObj.setCurrency("CNY");
        request.setAmount(amountObj);

        Payer payer = new Payer();
        payer.setOpenid(openid);
        request.setPayer(payer);
        return jsapiService.prepayWithRequestPayment(request);
    }

    public Map<String, String> buildPayParams(PrepayWithRequestPaymentResponse response) {
        Map<String, String> payParams = new HashMap<>();
        payParams.put("timeStamp", response.getTimeStamp());
        payParams.put("nonceStr", response.getNonceStr());
        payParams.put("package", "prepay_id=" + response.getPackageVal());
        payParams.put("signType", response.getSignType());
        payParams.put("paySign", response.getPaySign());
        return payParams;
    }

    private String generateOutTradeNo() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return "WORDER" + timestamp + random;
    }


}
