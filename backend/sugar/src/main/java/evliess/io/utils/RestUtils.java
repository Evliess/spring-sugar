package evliess.io.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

public class RestUtils {
    private static final String DPSK_CHAT_URL = "https://api.deepseek.com/chat/completions";
    private static final String HY_CHAT_URL = "https://api.hunyuan.cloud.tencent.com/v1/chat/completions";
    private static final String QW_CHAT_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String OA_CHAT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String SORRY_MESSAGE = "服务器繁忙，工程师正在抢修中，请稍后再试！";

    private static final String SYSTEM_MSG = """
            请根据我提供的中文名字及特征要求给出3个不同的英文名。必须按照下面格式回答：
            You must use this json schema:
            [{
                "名字": str,
                "寓意": str,
                "发音": str,
                "人物形象": str,
            },
            ...
            ]
            以下内容是你回答时的注意事项，回答时请务必遵守：
            <回答要求>
            针对列表中的每一项，必须按照一下要求回答：
            名字：英文名字(该英文名字对应的中文译名)
                - 示例：John(约翰)
            寓意：你的回复务必包含只包含起源，寓意，英文名组成解释，以及这个名字出名的原因！用60个汉字详细描述。(注意：这3个名字的寓意不能重复，必须使用不同的词语表达)
            发音：这个名字的音标和音节数。如果该发音和中文发音相似，请解释哪里相似！
                - 内容必须包含：音标和音节数。
                - 如果该发音和中文发音相似，请指出相似的那个英文和中文部分。
            人物形象：
                - 用大于100个字不超过300个字描述。
                - 四个名字的人物形象语言描述不能有重复描述，可以使用不同的词语或者语气，使答案看起来丰富多彩。
            流行度：这个名字在过去的流行度(注意：不包含欧美地区)。
            </回答要求>
            """;

    private static final String USER_MESSAGE = "中文名字：%1。 性别：%2。星座或者MBTI：%3。期望寓意: %4。其他要求: %5。是否需要和中文名字发音相似：%6。";
    private static final String DPSK_MODEL = "deepseek-chat";
    private static final String HY_MODEL = "hunyuan-turbo";
    private static final String QW_MODEl = "qwen-max";
    private static final String OA_MODEL = "gpt-4o-mini";

    private static final Double TEMPERATURE = 1.9;
    private static final Logger log = LoggerFactory.getLogger(RestUtils.class);

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
        return USER_MESSAGE.replace("%1", name).replace("%2", sex
                ).replace("%3", mbti).replace("%4", meaning)
                .replace("%5", other).replace("%6", voice);
    }

    public static String oaChat(String msg) throws JsonProcessingException {
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + System.getenv("OA_API_KEY"));
        String uuid = UUID.randomUUID().toString();
        HttpEntity<String> request = getStringHttpEntity(uuid, OA_MODEL, headers, msg);
        ResponseEntity<String> response = restTemplate.postForEntity(OA_CHAT_URL, request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String respBody = response.getBody();
            String result = convertOpenAIJsonResponse(respBody);
            log.info("OA response: {} - {}", uuid, result);
            return result;
        } else {
            System.err.println("Failed to chat with deepseek");
            return null;
        }
    }

    public static String dpskChat(String msg) throws JsonProcessingException {
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        System.out.println(System.getenv("API_KEY"));
        headers.add("Authorization", "Bearer " + System.getenv("API_KEY"));
        String uuid = UUID.randomUUID().toString();
        HttpEntity<String> request = getStringHttpEntity(uuid, DPSK_MODEL, headers, msg);
        ResponseEntity<String> response = restTemplate.postForEntity(DPSK_CHAT_URL, request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String respBody = response.getBody();
            String result = convertOpenAIJsonResponse(respBody);
            log.info("DP response: {} - {}", uuid, result);
            return result;
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
        String uuid = UUID.randomUUID().toString();
        HttpEntity<String> request = getStringHttpEntity(uuid, HY_MODEL, headers, msg);
        ResponseEntity<String> response = restTemplate
                .postForEntity(HY_CHAT_URL, request, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String respBody = response.getBody();
            String result = convertOpenAIJsonResponse(respBody);
            log.info("HY response: {} - {}", uuid, result);
            return result;
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
        String uuid = UUID.randomUUID().toString();
        HttpEntity<String> request = getStringHttpEntity(uuid, QW_MODEl, headers, msg);
        ResponseEntity<String> response = restTemplate
                .postForEntity(QW_CHAT_URL, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            String respBody = response.getBody();
            String result = convertOpenAIJsonResponse(respBody);
            log.info("QW response: {} - {}", uuid, result);
            return result;
        } else {
            System.err.println("Failed to chat with qw");
            return null;
        }
    }

    private static String convertOpenAIJsonResponse(String response) {
        if (response == null) {
            return SORRY_MESSAGE;
        }
        JSONObject jsonObject = JSON.parseObject(response);
        JSONArray choices = jsonObject.getJSONArray("choices");
        if (choices != null && !choices.isEmpty()) {
            JSONObject choice = (JSONObject) choices.get(0);
            JSONObject message = (JSONObject) choice.get("message");
            return message.getString("content");
        }
        return SORRY_MESSAGE;
    }

    private static HttpEntity<String> getStringHttpEntity(String uuid, String model, HttpHeaders headers, String msg) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_MSG));
        String userMessage = replaceUserMessage(msg);
        log.info("User message - {}: {}", uuid, userMessage);
        messages.add(Map.of("role", "user", "content", userMessage));
        body.put("messages", messages);
        body.put("model", model);
        body.put("stream", false);
        body.put("temperature", TEMPERATURE);
        return new HttpEntity<>(new ObjectMapper().writeValueAsString(body), headers);
    }
}
