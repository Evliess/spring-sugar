package evliess.io.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
}
