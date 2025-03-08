package evliess.io.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import evliess.io.entity.SugarToken;
import evliess.io.entity.SugarUser;
import evliess.io.jpa.SugarTokenRepository;
import evliess.io.jpa.SugarUserRepository;
import evliess.io.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class SugarUserService {

    private final SugarUserRepository sugarUserRepository;
    private final SugarTokenRepository sugarTokenRepository;

    @Autowired
    public SugarUserService(SugarUserRepository sugarUserRepository, SugarTokenRepository sugarTokenRepository) {
        this.sugarUserRepository = sugarUserRepository;
        this.sugarTokenRepository = sugarTokenRepository;
    }

    public void save(SugarToken sugarToken) {
        sugarTokenRepository.save(sugarToken);
    }

    public String findByUsername(String body) {
        try {
            JSONObject jsonNode = JSON.parseObject(body);
            String name = jsonNode.getString("name");
            String privateKey = jsonNode.getString("key");
            if (name == null || name.isEmpty()
                    || privateKey == null || privateKey.isEmpty()
                    || name.length() > 20
                    || (!privateKey.startsWith("-----BEGIN PRIVATE KEY-----") && !privateKey.endsWith("-----END PRIVATE KEY-----"))) {
                System.err.println("User: " + name + " not found!");
                return Instant.now().minusSeconds(60).toEpochMilli() + "";
            }
            SugarUser sugarUser = sugarUserRepository.findByUsername(name);
            if (sugarUser == null) {
                System.err.println("User: " + name + " not found!");
                return Instant.now().minusSeconds(60).toEpochMilli() + "";
            }
            if (EncryptUtils.verifyUser(privateKey, name, sugarUser.getAccessKey())) {
                System.out.println("User: " + name + " verified!");
                return Instant.now().plus(Duration.ofDays(1)).toEpochMilli() + "";
            } else {
                System.err.println("User: " + name + " not found!");
                return Instant.now().minusSeconds(60).toEpochMilli() + "";
            }
        } catch (Exception e) {
            System.err.println("User:not found!");
            return Instant.now().minusSeconds(60).toEpochMilli() + "";
        }

    }
}
