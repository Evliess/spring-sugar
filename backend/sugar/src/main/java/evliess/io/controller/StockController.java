package evliess.io.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.service.*;
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

    @Autowired
    private SugarService service;
    @Autowired
    private DpskService dpskService;

    @Autowired
    private QwService qwService;

    @Autowired
    private HunYService hunYService;

    @Autowired
    private AuditTokenService auditTokenService;

    private static final Logger log = LoggerFactory.getLogger(StockController.class);

    @GetMapping("/private/stocks")
    public ResponseEntity<String> getStock() throws JsonProcessingException {
        return ResponseEntity.ok("123Test");
    }

    @PostMapping("/private/sugar")
    public ResponseEntity<String> getSugar(@RequestBody String body) throws JsonProcessingException {
        log.info(body);
        String result = service.chat(body);
        if (null == result) {
            result = service.chat(body);
        }
        log.info(result);
        return ResponseEntity.ok(result);
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
