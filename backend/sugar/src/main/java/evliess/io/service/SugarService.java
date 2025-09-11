package evliess.io.service;

import evliess.io.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
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

    private CompletableFuture<String> createFuture(String message, String apiKeyEnv) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (apiKeyEnv.equals("QW_API_KEY")) {
//                    return RestUtils.jsonArrayToString(qwService.chat(message, System.getenv(apiKeyEnv)));
                    return null;
                } else {
                    return RestUtils.jsonArrayToString(dpskService.chat(message, System.getenv(apiKeyEnv)));
                }
            } catch (Exception e) {
                log.error("{}", e.getMessage(), e);
                return null;
            }
        }).thenApply(answer -> {
            if (answer != null && !answer.isEmpty()) {
                return answer;
            }
            return null;
        }).exceptionally(e -> null);
    }

    public String chat(String message) {
        CompletableFuture<String> future1 = createFuture(message, "QW_API_KEY");
        CompletableFuture<String> future2 = createFuture(message, "API_KEY0");
        CompletableFuture<String> future3 = createFuture(message, "API_KEY1");
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        futureList.add(future1);
        futureList.add(future2);
        futureList.add(future3);
        CompletableFuture<Object> firstValid = CompletableFuture.anyOf(
                futureList.toArray(new CompletableFuture[0])
        );
        CompletableFuture<Void> allDone = CompletableFuture.allOf(
                futureList.toArray(new CompletableFuture[0])
        );
        AtomicReference<String> finalResult = new AtomicReference<>("");
        CompletableFuture.anyOf(firstValid, allDone).thenAcceptAsync(result -> {
                    if (result instanceof String) {
                        finalResult.set((String) result);
                    } else {
                        String validResult = futureList.stream()
                                .map(f -> {
                                    try {
                                        return f.get();
                                    } catch (Exception e) {
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull).findFirst().orElse(null);
                        finalResult.set(validResult);
                    }
                }).join();
        String response;
        if (finalResult.get() != null && !finalResult.get().isEmpty()) {
            response = finalResult.get();
        } else {
            response = "请点击返回修改试试！";
        }
        return response;
    }
}
