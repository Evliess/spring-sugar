package evliess.io.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.service.DpskService;
import evliess.io.service.QwService;
import evliess.io.service.SugarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    @Autowired
    private SugarService service;

    @GetMapping("/public/stocks")
    public ResponseEntity<String> getStock() throws JsonProcessingException {
        return ResponseEntity.ok("123Test");
    }

    @PostMapping("/public/sugar")
    public ResponseEntity<String> getSugar(@RequestBody String body) throws JsonProcessingException {
        return ResponseEntity.ok(service.chat(body));
    }

}
