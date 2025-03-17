package evliess.io.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import evliess.io.config.Constants;
import evliess.io.service.AuditTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuditTokenController {

    private final AuditTokenService auditTokenService;

    @Autowired
    public AuditTokenController(AuditTokenService auditTokenService) {
        this.auditTokenService = auditTokenService;
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
}
