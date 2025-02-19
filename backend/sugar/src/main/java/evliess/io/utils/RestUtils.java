package evliess.io.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestUtils {
    private static final String CHAT_URL = "https://api.deepseek.com/chat/completions";
    private static final String QW_CHAT_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String SYSTEM_MESSAGE =
            """
                    你精通星座分析，易经八卦，占卜预测。请根据下面提供的内容，为我做深层次的综合分析并且做预测。
                    回答必须条例清晰，逻辑严密，不得有任何疏漏。
                    回答格式如下：
                    ### 一、星座分析
                    ### 二、易经八卦
                    ### 三、占卜预测
                    """;
    private static final String USER_MESSAGE = "名字：%1，性别：%2，出生时间：%3，出生地：北京市。";
    private static final String MODEL = "deepseek-chat";


    private static String replaceUserMessage(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String name = jsonNode.get("name").asText();
        String sex = jsonNode.get("sex").asText();
        String birth = jsonNode.get("birth").asText();
        return USER_MESSAGE.replace("%1", name).replace("%2", sex
        ).replace("%3", birth);
    }

    public static String dpskChat(String msg) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + System.getenv("API_KEY"));
        HttpEntity<String> request = getStringHttpEntity(headers, msg);
        ResponseEntity<String> response = restTemplate.postForEntity(CHAT_URL, request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String respBody = response.getBody();
            return convertDpskJsonResponse(respBody);
        } else {
            System.err.println("Failed to chat with deepseek");
            return null;
        }
    }

    public static String qwChat(String msg) throws JsonProcessingException {
        System.out.println("qwChat answer");
        String model = "qwen-plus";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + System.getenv("QW_API_KEY"));
        // 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", SYSTEM_MESSAGE),
                Map.of("role", "user", "content", msg)
        );
        requestBody.put("messages", messages);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                QW_CHAT_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return convertQwJsonResponse(response.getBody());
        } else {
            System.err.println("Failed to chat with qw");
            return null;
        }
    }

    private static String convertQwJsonResponse(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode choices = jsonNode.get("choices");
        if (choices.isArray()) {
            JsonNode choice = choices.get(0);
            JsonNode message = choice.get("message");
            return message.get("content").asText();
        }
        return "请修改输入，再试一次！";
    }

    private static String convertDpskJsonResponse(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode choices = jsonNode.get("choices");
        if (choices.isArray()) {
            JsonNode choice = choices.get(0);
            JsonNode message = choice.get("message");
            return message.get("content").asText();
        }
        return "请修改输入，再试一次！";
    }

    private static HttpEntity<String> getStringHttpEntity(HttpHeaders headers, String msg) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_MESSAGE));
        messages.add(Map.of("role", "user", "content", replaceUserMessage(msg)));
        body.put("messages", messages);
        body.put("model", MODEL);
        body.put("stream", false);
        return new HttpEntity<>(new ObjectMapper().writeValueAsString(body), headers);
    }
}
