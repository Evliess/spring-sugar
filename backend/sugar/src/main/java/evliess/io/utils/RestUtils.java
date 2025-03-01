package evliess.io.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestUtils {
    private static final String DPSK_CHAT_URL = "https://api.deepseek.com/chat/completions";
    private static final String HY_CHAT_URL = "https://api.hunyuan.cloud.tencent.com/v1/chat/completions";
    private static final String QW_CHAT_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String OA_CHAT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String SORRY_MESSAGE = "服务器繁忙，工程师正在抢修中，请稍后再试！";
    private static final String SYSTEM_MESSAGE =
    """
            你精通星座分析，易经八卦，占卜预测。请根据下面提供的内容，为我做深层次的综合分析并且做预测。
            回答必须条例清晰，逻辑严密，不得有任何疏漏。
            回答格式如下：
            ### 一、星座分析
            ### 二、易经八卦
            ### 三、占卜预测
            """;

    private static final String SYSTEM_MSG = """
            请根据我提供的中文名字及特征要求给出4个不同的英文名。必须按照下面格式回答：
            <回答格式> //请注意，该行不需要回复！
            英文名字(该英文名字对应的中文名字)
            寓意:
            发音:
            人物形象:
            流行度:
            </回答格式> //请注意，该行不需要回复！

            <回答要求>
            针对列表中的每一项，必须按照一下要求回答：
            寓意：如果你知道这个寓意的起源，务必包含起源信息。不要超过100个字。
            发音：这个名字的音标和音节数。
            人物形象：用大于100个字不超过200个字描述。
            流行度：这个名字在过去的流行度。
            </回答要求>
            
            下面我给出一个回答的例子：
            1. 英文名字: Yumi (尤米)
            寓意:  Yumi 是一个日语名字，也常见于韩国和中国。它通常是由“优”（美丽、优秀）和“美”（美丽、善良）两个字组合而成，带有清新且可爱的感觉。代表“美丽的”和“优雅的”，象征着一个聪明、优雅而又独特的女性。它传递出一种温暖、善良、独立的感觉。
            发音: /juːˈmi/；音节数为 2（Yu-mi）。
            人物形象: Yumi 是一个温柔而有魅力的女孩。她个性内敛，具有独立性和智慧。她可能是那种充满正能量、擅长平衡工作和生活的女性，既能独立又能关心他人。
            发音: /juːˈmi/；音节数为 2（Yu-mi）。
            人物形象: Yumi 是一个温柔而有魅力的女孩。她个性内敛，具有独立性和智慧。她可能是那种充满正能量、擅长平衡工作和生活的女性，既能独立又能关心他人。
            """;

    private static final String USER_MESSAGE = "中文名字：%1。 性别：%2。星座或者MBTI：%3。期望寓意: %4。其他要求: %5。是否需要和中文名字发音相似：%6。";
    private static final String DPSK_MODEL = "deepseek-chat";
    private static final String HY_MODEL = "hunyuan-turbo";
    private static final String QW_MODEl = "qwen-max";
    private static final String OA_MODEL = "gpt-4o-mini";
    private static RestTemplate buildRestTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofMinutes(2l))
                .setReadTimeout(Duration.ofMinutes(2l)).build();
    }

    private static String replaceUserMessage(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String name = jsonNode.get("name").asText();
        String sex = jsonNode.get("sex").asText();
        String mbti = jsonNode.get("mbti").asText();
        String meaning = jsonNode.get("meaning").asText();
        String other = jsonNode.get("other").asText();
        String voice = jsonNode.get("voice").asText();
        String userMessage = USER_MESSAGE.replace("%1", name).replace("%2", sex
                ).replace("%3", mbti).replace("%4", meaning)
                .replace("%5", other).replace("%6", voice);
        System.out.println(userMessage);
        return userMessage;
    }

    public static String oaChat(String msg) throws JsonProcessingException {
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + System.getenv("OA_API_KEY"));
        HttpEntity<String> request = getStringHttpEntity(OA_MODEL, headers, msg);
        ResponseEntity<String> response = restTemplate.postForEntity(OA_CHAT_URL, request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String respBody = response.getBody();
            return convertOpenAIJsonResponse(respBody);
        } else {
            System.err.println("Failed to chat with deepseek");
            return null;
        }
    }

    public static String dpskChat(String msg) throws JsonProcessingException {
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + System.getenv("API_KEY"));
        HttpEntity<String> request = getStringHttpEntity(DPSK_MODEL, headers, msg);
        ResponseEntity<String> response = restTemplate.postForEntity(DPSK_CHAT_URL, request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String respBody = response.getBody();
            return convertOpenAIJsonResponse(respBody);
        } else {
            System.err.println("Failed to chat with deepseek");
            return null;
        }
    }

    public static String hunYChat(String msg) throws JsonProcessingException {
        System.out.println("HY is answering...");
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + System.getenv("HY_API_KEY"));
        HttpEntity<String> request = getStringHttpEntity(HY_MODEL, headers, msg);
        ResponseEntity<String> response = restTemplate
                .postForEntity(HY_CHAT_URL, request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String respBody = response.getBody();
            return convertOpenAIJsonResponse(respBody);
        } else {
            System.err.println("Failed to chat with deepseek");
            return null;
        }
    }

    public static String qwChat(String msg) throws JsonProcessingException {
        System.out.println("QW is answering...");
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + System.getenv("QW_API_KEY"));
        HttpEntity<String> request = getStringHttpEntity(QW_MODEl, headers, msg);
        ResponseEntity<String> response = restTemplate
                .postForEntity(QW_CHAT_URL, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return convertOpenAIJsonResponse(response.getBody());
        } else {
            System.err.println("Failed to chat with qw");
            return null;
        }
    }

    private static String convertOpenAIJsonResponse(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (response == null) {
            return SORRY_MESSAGE;
        }
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode choices = jsonNode.get("choices");
        if (choices.isArray()) {
            JsonNode choice = choices.get(0);
            JsonNode message = choice.get("message");
            return message.get("content").asText();
        }
        return SORRY_MESSAGE;
    }

    private static HttpEntity<String> getStringHttpEntity(String model, HttpHeaders headers, String msg) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_MSG));
        messages.add(Map.of("role", "user", "content", replaceUserMessage(msg)));
        body.put("messages", messages);
        body.put("model", model);
        body.put("stream", false);
        body.put("temperature", 1.1);
        return new HttpEntity<>(new ObjectMapper().writeValueAsString(body), headers);
    }
}
