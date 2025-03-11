package evliess.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.interfaces.Inference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class SugarService implements Inference {
    private static final Logger log = LoggerFactory.getLogger(SugarService.class);


    @Autowired
    private DpskService dpskService;

    @Autowired
    private QwService qwService;

    @Autowired
    private HunYService hunYService;

    @Autowired
    private OAService oaService;


    @Override
    public String chat(String message) throws JsonProcessingException {
        Duration timeout = Duration.ofMinutes(1);
        String response;
        CompletableFuture<String> dpskResponseFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return dpskService.chat(message);
            } catch (Exception e) {
                log.error("Error in dpsk.chat", e);
                return "";
            }
        });
        try {
            response = dpskResponseFuture.get(timeout.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
            if (response != null && !response.isEmpty()) {
                return response;
            }
        } catch (Exception e) {
            CompletableFuture<String> qwResponseFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return qwService.chat(message);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            });
            response = qwResponseFuture.join();

        }
        log.info(response);
        return response;
    }

    @Override
    public String chat(String message, String userPrompt, String systemPrompt) {
        return "null";
    }
}
