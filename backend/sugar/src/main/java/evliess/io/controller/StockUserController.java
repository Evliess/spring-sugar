package evliess.io.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.entity.SugarToken;
import evliess.io.service.SugarUserService;
import evliess.io.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockUserController {
    private final SugarUserService sugarUserService;

    @Autowired
    public StockUserController(SugarUserService sugarUserService) {
        this.sugarUserService = sugarUserService;
    }

    @PostMapping("/public/user")
    public ResponseEntity<String> verify(@RequestBody String body) {
        String token = sugarUserService.findByUsername(body);

        JSONObject jsonObject = new JSONObject();
        if (token.endsWith("T")) {
            jsonObject.put("token", token.replace("T", ""));
        } else {
            jsonObject.put("token", "F");
        }
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/public/tokens")
    public ResponseEntity<String> generateToken(@RequestBody String body) throws JsonProcessingException {
        if (body == null || body.isEmpty()) {
            return ResponseEntity.ok("Invalid request");
        }
        JSONObject jsonNode = JSON.parseObject(body);
        Integer days = jsonNode.getInteger("days");
        String token = TokenUtils.generateToken(days);
        sugarUserService.save(new SugarToken(token, ""));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return ResponseEntity.ok(jsonObject.toString());
    }

}
