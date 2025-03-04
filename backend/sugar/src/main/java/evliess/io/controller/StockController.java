package evliess.io.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import evliess.io.service.DpskService;
import evliess.io.service.HunYService;
import evliess.io.service.QwService;
import evliess.io.service.SugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class StockController {

    @Autowired
    private SugarService service;
    @Autowired
    private DpskService dpskService;

    @Autowired
    private QwService qwService;

    @Autowired
    private HunYService hunYService;

    @GetMapping("/public/stocks")
    public ResponseEntity<String> getStock() throws JsonProcessingException {
        return ResponseEntity.ok("123Test");
    }

    @PostMapping("/public/sugar")
    public ResponseEntity<String> getSugar(@RequestBody String body) throws JsonProcessingException {
        return ResponseEntity.ok(service.chat(body));
    }

    @PostMapping("/public/tokens")
    public ResponseEntity<String> generateToken(@RequestBody String body) {
        String uuid = UUID.randomUUID() + "-" + System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", uuid);
        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/public/dp")
    public ResponseEntity<String> getSugar0(@RequestBody String body) throws JsonProcessingException {
        return ResponseEntity.ok(dpskService.chat(body));
    }

    @PostMapping("/public/hy")
    public ResponseEntity<String> getSugar1(@RequestBody String body) throws JsonProcessingException {
        return ResponseEntity.ok(hunYService.chat(body));
    }

    @PostMapping("/public/qw")
    public ResponseEntity<String> getSugar2(@RequestBody String body) throws JsonProcessingException {
        return ResponseEntity.ok(qwService.chat(body));
    }

}
