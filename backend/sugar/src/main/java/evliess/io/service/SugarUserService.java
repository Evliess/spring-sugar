package evliess.io.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import evliess.io.entity.SugarToken;
import evliess.io.entity.SugarUser;
import evliess.io.jpa.SugarTokenRepository;
import evliess.io.jpa.SugarUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class SugarUserService {
    private static final Logger log = LoggerFactory.getLogger(SugarUserService.class);

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
                    ) {
                log.error("User: {} not found!", name);
                return Instant.now().minusSeconds(60).toEpochMilli() + "";
            }
            SugarUser sugarUser = sugarUserRepository.findByUsername(name);
            if (sugarUser == null) {
                log.error("User: {} not found!", name);
                return Instant.now().minusSeconds(60).toEpochMilli() + "";
            }
            if (name.equals(sugarUser.getAccessKey())) {
                log.info("User: {} verified!", name);
                return Instant.now().plus(Duration.ofDays(1)).toEpochMilli() + "T";
            } else {
                log.error("User: {} not found!", name);
                return Instant.now().minusSeconds(60).toEpochMilli() + "";
            }
        } catch (Exception e) {
            log.error("User:not found!");
            return Instant.now().minusSeconds(60).toEpochMilli() + "";
        }

    }
}
