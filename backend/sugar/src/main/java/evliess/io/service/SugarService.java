package evliess.io.service;

import evliess.io.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SugarService {
    private static final Logger log = LoggerFactory.getLogger(SugarService.class);
    private final DpskService dpskService;
    private final QwService qwService;

    @Autowired
    public SugarService(DpskService dpskService, QwService qwService) {
        this.dpskService = dpskService;
        this.qwService = qwService;
    }

    public String chat(String message) {
        String response;
        AtomicReference<String> dpskResponse0 = new AtomicReference<>("");
        AtomicReference<String> dpskResponse1 = new AtomicReference<>("");
        AtomicReference<String> qwResponse = new AtomicReference<>("");
        int numThreads = 3;
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            executor.submit(() -> {
                try {
                    dpskResponse0.set(RestUtils.jsonArrayToString(dpskService.chat(message, System.getenv("API_KEY0"))));
                } catch (Exception e) {
                    dpskResponse0.set(null);
                    log.error("Error in dpsk.chat", e);
                } finally {
                    latch.countDown();
                }
            });
            executor.submit(() -> {
                try {
                    dpskResponse1.set(RestUtils.jsonArrayToString(dpskService.chat(message, System.getenv("API_KEY1"))));
                } catch (Exception e) {
                    dpskResponse1.set(null);
                    log.error("Error in dpsk.chat", e);
                } finally {
                    latch.countDown();
                }
            });
            executor.submit(() -> {
                try {
                    qwResponse.set(RestUtils.jsonArrayToString(qwService.chat(message, System.getenv("QW_API_KEY"))));
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


        if (dpskResponse0.get() != null && !dpskResponse0.get().isEmpty()) {
            response = dpskResponse0.get();
        } else if (dpskResponse1.get() != null && !dpskResponse1.get().isEmpty()) {
            response = dpskResponse1.get();
        } else if (qwResponse.get() != null && !qwResponse.get().isEmpty()) {
            response = qwResponse.get();
        } else {
            response = "请点击返回编辑试试！";
        }
        return response;
    }
}
