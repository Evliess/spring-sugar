package evliess.io.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import evliess.io.entity.SugarUserHistory;
import evliess.io.jpa.SugarUserHistoryRepository;
import evliess.io.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final SugarUserHistoryRepository sugarUserHistoryRepository;

    @Autowired
    public SugarService(DpskService dpskService, QwService qwService, SugarUserHistoryRepository sugarUserHistoryRepository) {
        this.dpskService = dpskService;
        this.qwService = qwService;
        this.sugarUserHistoryRepository = sugarUserHistoryRepository;
    }

    private void saveChatHistory(String message, String llmResp, UsernamePasswordAuthenticationToken authenticationToken) {
        try {
            JSONArray jsonArray = RestUtils.convertRespToJSONArray(llmResp);
            if (jsonArray == null) return;
            String principal = authenticationToken.getPrincipal().toString();
            String username = principal.split("::")[0];
            StringBuilder sbHistoryNames = new StringBuilder();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                sbHistoryNames.append(jsonObject.getString("名字")).append(",");
            }
            saveHistoryMsg(message, username, sbHistoryNames);
        } catch (Exception e) {
            log.error("Failed to save chat history: {}", message, e);
        }
    }

    private void saveHistoryMsg(String message, String username, StringBuilder sbHistoryNames) {
        SugarUserHistory sugarUserHistory = sugarUserHistoryRepository.findByNameAndMsg(username, message);
        if (sugarUserHistory == null) {
            sugarUserHistory = new SugarUserHistory(username, message, sbHistoryNames.toString());
        } else {
            String history = sugarUserHistory.getHistory();
            int count = 0;
            for (char ch : history.toCharArray()) {
                if (ch == ',') count++;
            }
            if (count >= 90) {
                sugarUserHistory.setHistory(sbHistoryNames.toString());
            } else {
                StringBuilder sbHistory = new StringBuilder(history);
                String[] names = sbHistoryNames.toString().split(",");
                for (String name : names) {
                    if (!history.contains(name)) {
                        sbHistory.append(name).append(",");
                    }
                }
                sugarUserHistory.setHistory(sbHistory.toString());
            }
        }
        sugarUserHistoryRepository.save(sugarUserHistory);
    }

    private CompletableFuture<String> createFuture(String message, String apiKeyEnv) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (apiKeyEnv.equals("QW_API_KEY")) {
//                    return RestUtils.jsonArrayToString(qwService.chat(message, System.getenv(apiKeyEnv)));
                    return null;
                } else {
                    String principal = authenticationToken.getPrincipal().toString();
                    String username = principal.split("::")[0];
                    SugarUserHistory sugarUserHistory = sugarUserHistoryRepository.findByNameAndMsg(username, message);
                    String history = "";
                    if (sugarUserHistory != null && !sugarUserHistory.getHistory().isEmpty()) {
                        history = sugarUserHistory.getHistory();
                    }
                    String llmResp = dpskService.chat(message, history, System.getenv(apiKeyEnv));
                    saveChatHistory(message, llmResp, authenticationToken);
                    return RestUtils.jsonArrayToString(llmResp);
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
