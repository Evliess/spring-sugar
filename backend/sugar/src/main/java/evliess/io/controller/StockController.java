package evliess.io.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.config.Constants;
import evliess.io.service.*;
import evliess.io.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/private/stocks")
    public ResponseEntity<String> getStock() throws JsonProcessingException {
        return ResponseEntity.ok("123Test");
    }

    @PostMapping("/private/sugar")
    public ResponseEntity<String> getSugar(@RequestBody String body) throws JsonProcessingException {
        System.out.println(body);
        String result = service.chat(body);
        try {
            result = RestUtils.jsonArrayToString(result);
        } catch (Exception e) {
            System.err.println("LLM response format error!");
            result = RestUtils.jsonArrayToString(service.chat(body));
        } finally {
            try {
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                String principal = authenticationToken.getPrincipal().toString();
                String[] info = principal.split(Constants.DOUBLE_COLON);
                auditTokenService.saveAudit(info[0], info[1]);
            } catch (Exception e) {
                System.err.println("LLM save audit error!");
            }

        }
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
