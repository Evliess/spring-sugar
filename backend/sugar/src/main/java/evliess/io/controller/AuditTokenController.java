package evliess.io.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import evliess.io.config.Constants;
import evliess.io.entity.AuditToken;
import evliess.io.jpa.SugarUserHistoryRepository;
import evliess.io.jpa.UserRespHistoryRepository;
import evliess.io.service.AuditTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
public class AuditTokenController {
    private static final Logger log = LoggerFactory.getLogger(AuditTokenController.class);


    private final AuditTokenService auditTokenService;
    private final SugarUserHistoryRepository sugarUserHistoryRepository;
    private final UserRespHistoryRepository userRespHistoryRepository;

    @Autowired
    public AuditTokenController(AuditTokenService auditTokenService,
                                SugarUserHistoryRepository sugarUserHistoryRepository,
                                UserRespHistoryRepository userRespHistoryRepository) {
        this.auditTokenService = auditTokenService;
        this.sugarUserHistoryRepository = sugarUserHistoryRepository;
        this.userRespHistoryRepository = userRespHistoryRepository;
    }

    @GetMapping("/public/audit/users")
    public ResponseEntity<String> getUsers() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(this.auditTokenService.findUsersByToken());
        return ResponseEntity.ok(jsonArray.toString());
    }

    @PostMapping("/public/audit/detail")
    public ResponseEntity<String> getAudit(@RequestBody String body) {
        JSONObject jsonNode = JSON.parseObject(body);
        String user = jsonNode.getString("user");
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(this.auditTokenService.findByUser(user));
        return ResponseEntity.ok(jsonArray.toString());
    }

    @PostMapping("/public/audit/time-span")
    public ResponseEntity<String> getAuditByTimeSpan(@RequestBody String body) {
        JSONObject jsonNode = JSON.parseObject(body);
        String timeSpan = jsonNode.getString("timeSpan");
        JSONArray jsonArray = new JSONArray();
        if (Constants.LATEST_24H.equals(timeSpan)) {
            jsonArray.addAll(this.auditTokenService.findLast24H());
        } else if (Constants.LATEST_3D.equals(timeSpan)) {
            jsonArray.addAll(this.auditTokenService.findLast3D());
        } else if (Constants.LATEST_7D.equals(timeSpan)) {
            jsonArray.addAll(this.auditTokenService.findLast7D());
        } else if (Constants.LATEST_30D.equals(timeSpan)) {
            jsonArray.addAll(this.auditTokenService.findLast30D());
        } else {
            jsonArray.addAll(this.auditTokenService.findLast24H());
        }
        return ResponseEntity.ok(jsonArray.toString());
    }

    @PostMapping("/public/audit/user-token")
    public ResponseEntity<String> findLatestValidToken(@RequestBody String body) {
        JSONObject jsonNode = JSON.parseObject(body);
        String openid = jsonNode.getString("openId");
        log.info("Try to get latest valid token with openId: {}", openid);
        AuditToken auditToken = this.auditTokenService.findLatestValidToken(openid);
        JSONObject jsonObject = new JSONObject();
        if (auditToken == null) {
            jsonObject.put("token", "token");
            return ResponseEntity.ok(jsonObject.toString());
        }
        jsonObject.put("token", auditToken.getToken());
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/private/audit/check-token")
    public ResponseEntity<String> isValidToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "ok");
        return ResponseEntity.ok(jsonObject.toString());
    }

    @DeleteMapping("/public/audit/now-minus-days")
    public ResponseEntity<String> deleteNowMinusDays(@RequestBody String body) {
        JSONObject jsonNode = JSON.parseObject(body);
        String days = jsonNode.getString("days");
        Long timestamp = Instant.now().plus(Duration.ofDays(Long.parseLong(days))).toEpochMilli();
        int historyNames = sugarUserHistoryRepository.deleteLessThanConsumedAt(timestamp);
        int records = auditTokenService.deleteLessThanConsumedAt(timestamp);
        int historyResp = sugarUserHistoryRepository.deleteLessThanConsumedAt(timestamp);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "ok");
        jsonObject.put("records", records);
        jsonObject.put("historyNames", historyNames);
        jsonObject.put("historyResp", historyResp);
        return ResponseEntity.ok(jsonObject.toString());


    }
}
