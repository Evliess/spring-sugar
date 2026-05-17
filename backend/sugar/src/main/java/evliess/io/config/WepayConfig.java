package evliess.io.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
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
    @Value("${wxpay.private-key-path}")
    private String privateKeyPath;
    @Value("${wxpay.mch-serial-no}")
    private String mchSerialNo;

    @Value("${wxpay.connect-timeout}")
    private int connectTimeout;
    @Value("${wxpay.read-timeout}")
    private int readTimeout;


    @Value("${wxpay.appid}")
    private String appId;


    /**
     * 1. 将支付配置独立为一个 Bean，供支付服务和回调验签共用
     */
    @Bean
    public Config wxPayConfig() {
        // 【关键】请根据你项目实际使用的 Config 实现类来替换这里
        // 如果你用的是微信支付平台证书（自动更新），就是 RSAAutoCertificateConfig
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(mchId)
                .privateKeyFromPath(privateKeyPath)
                .merchantSerialNumber(mchSerialNo)
                .apiV3Key(apiV3Key)
                .build();

        // 如果你用的是微信支付公私钥，请用下面的代码并注释掉上面的
        // return new RSAPublicKeyConfig.Builder()
        //         .merchantId(mchId)
        //         .privateKeyFromPath(privateKeyPath)
        //         .merchantSerialNumber(mchSerialNo)
        //         .apiV3Key(apiV3Key)
        //         .publicKeyFromPath(publicKeyPath)
        //         .publicKeyId(publicKeyId)
        //         .build();
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
