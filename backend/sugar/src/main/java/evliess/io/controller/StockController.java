package evliess.io.controller;

import com.alibaba.fastjson2.JSONObject;
import evliess.io.config.Constants;
import evliess.io.service.AuditTokenService;
import evliess.io.service.SugarService;
import evliess.io.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {
    private static final Logger log = LoggerFactory.getLogger(StockController.class);

    private final SugarService service;
    private final AuditTokenService auditTokenService;

    @Autowired
    public StockController(SugarService service, AuditTokenService auditTokenService) {
        this.service = service;
        this.auditTokenService = auditTokenService;
    }


    @GetMapping("/private/stocks")
    public ResponseEntity<String> getStock() {
        return ResponseEntity.ok("123Test");
    }

    @PostMapping("/private/sugar")
    public ResponseEntity<String> getSugar(@RequestBody String body) {
        String checkResult = RestUtils.checkParams(body);
        if (!checkResult.equals(Constants.VERIFIED)) {
            return ResponseEntity.ok(checkResult);
        }
        String result = service.chat(body);
        if (null == result) {
            result = service.chat(body);
        }
        auditTokenService.saveAuditToken(Constants.TYPE_LLM);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/private/historyResp")
    public ResponseEntity<String> findUserRespHistoryByUsername(@RequestBody String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        String name = jsonObject.getString("name");
        return service.findUserRespHistoryByUsername(name);
    }


}
