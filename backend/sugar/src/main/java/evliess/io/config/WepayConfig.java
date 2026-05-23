package evliess.io.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WepayConfig {
    @Value("${wxpay.mchid}")
    private String mchId;
    @Value("${wxpay.api-v3-key}")
    private String apiV3Key;
    @Value("${wxpay.mch-serial-no}")
    private String mchSerialNo;
    @Value("${wxpay.connect-timeout}")
    private int connectTimeout;
    @Value("${wxpay.read-timeout}")
    private int readTimeout;
    @Value("${wxpay.pub-key-id}")
    private String pubkeyId;
    @Value("${wxpay.appid}")
    private String appId;
    @Value("${wxpay.apiclient_key_path}")
    private String apiclientKeyPath;
    @Value("${wxpay.pub_key_path}")
    private String pubKeyPath;

    /**
     * 1. 将支付配置独立为一个 Bean，供支付服务和回调验签共用
     */
    @Bean
    public Config wxPayConfig() {
        // 使用微信支付公私钥
         return new RSAPublicKeyConfig.Builder()
                 .merchantId(mchId)
                 .privateKeyFromPath(apiclientKeyPath)
                 .merchantSerialNumber(mchSerialNo)
                 .apiV3Key(apiV3Key)
                 .publicKeyFromPath(pubKeyPath)
                 .publicKeyId(pubkeyId)
                 .build();
    }

    /**
     * 2. 注入共用的 Config Bean 来创建 JsapiServiceExtension
     */
    @Bean
    public JsapiServiceExtension jsapiServiceExtension(Config wxPayConfig) {
        return new JsapiServiceExtension.Builder()
                .config(wxPayConfig)
                .build();
    }
}
