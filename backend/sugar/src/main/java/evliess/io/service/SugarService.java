package evliess.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.interfaces.Inference;
import evliess.io.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

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
        String response;
        AtomicReference<String> dpskResponse = new AtomicReference<>("");
        AtomicReference<String> qwResponse = new AtomicReference<>("");
        int numThreads = 2;
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            try {
                dpskResponse.set(RestUtils.jsonArrayToString(dpskService.chat(message)));
            } catch (Exception e) {
                dpskResponse.set(null);
                log.error("Error in dpsk.chat", e);
            } finally {
                latch.countDown();
            }
        });
        executor.submit(() -> {
            try {
                qwResponse.set(RestUtils.jsonArrayToString(qwService.chat(message)));
            } catch (Exception e) {
                qwResponse.set(null);
                log.error("Error in qw.chat", e);
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("Error in latch.await", e);
        }
        executor.shutdown();

        if (dpskResponse.get() != null) {
            response = dpskResponse.get();
        } else if (qwResponse.get() != null) {
            response = qwResponse.get();
        } else {
            response = null;
        }
        return response;
    }

    @Override
    public String chat(String message, String userPrompt, String systemPrompt) {
        return "null";
    }
}
