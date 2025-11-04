package evliess.io.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import evliess.io.entity.SugarToken;
import evliess.io.service.SugarUserService;
import evliess.io.utils.RestUtils;
import evliess.io.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockUserController {
    private final SugarUserService sugarUserService;
    private static final Logger log = LoggerFactory.getLogger(StockUserController.class);

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

    @PostMapping("/private/tokens")
    public ResponseEntity<String> generateToken(@RequestBody String body) {
        if (body == null || body.isEmpty()) {
            return ResponseEntity.ok("Invalid request");
        }
        JSONObject jsonNode = JSON.parseObject(body);
        String days = jsonNode.getString("days");
        String token = TokenUtils.generateToken(days);
        sugarUserService.save(new SugarToken(token, ""));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/private/token/deactive")
    public ResponseEntity<String> deactiveToken(@RequestBody String body) {
        JSONObject jsonNode = JSON.parseObject(body);
        String token = jsonNode.getString("token");
        sugarUserService.deactiveToken(token);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "deactived");
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/public/uid")
    public ResponseEntity<String> getUserId(@RequestBody String body) {
        JSONObject jsonNode = JSON.parseObject(body);
        String code = jsonNode.getString("code");
        String type = jsonNode.getString("type");
        log.info("req code, type: {},{}", code, type);
        String openId = RestUtils.getUid(code, type);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("openId", openId);
        log.info("req openid of code: {}", openId);
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/public/search/name")
    public ResponseEntity<String> searchName(@RequestBody String body) {
        log.info(body);
        JSONObject jsonNode = JSON.parseObject(body);
        String name = jsonNode.getString("name");
        JSONObject jsonObject = new JSONObject();
        if (name == null || name.isEmpty()) {
            jsonObject.put("name", "请输入名字或者名字开头的字母");
            return ResponseEntity.ok(jsonObject.toString());
        }
        jsonObject.put("name", "Jack");
        return ResponseEntity.ok("- Jack \n - Jack");
    }


}
