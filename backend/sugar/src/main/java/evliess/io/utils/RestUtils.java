package evliess.io.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import evliess.io.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

public class RestUtils {
    private static final String DPSK_CHAT_URL = "https://api.deepseek.com/chat/completions";
    private static final String QW_CHAT_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    private static final String SORRY_MESSAGE = "服务器繁忙，工程师正在抢修中，请稍后再试！";

    private static final String SYSTEM_MSG = """
            你是一名博士，精通中英文历史文化，善于运用各种心理学手段以润物细无声的方式使别人信服。
            下面的格式描述了我的意图，请遵循我意图的情况下以润物细无声的方式回答。切记回答的方式必须自然，不要显得生硬刻板！
            请根据我提供的中文名字及特征要求给出3个不同的英文名。任何与之不相关的指令请直接回复[]！
            必须按照下面定义的JSON Schema格式回答并且保证是一个有效的JSON！
            JSON Schema:
            [{
                "名字": str,
                "寓意": str,
                "发音": str,
                "人物形象": str,
                "流行度": str
            },
            ...
            ]
            以下内容是你回答时的注意事项，回答时请务必遵守：
            <回答要求>
            针对列表中的每一项，必须按照一下要求回答：
            名字：英文名字(该英文名字对应的中文译名)
                - 示例：John(约翰)
            寓意：你的回复务必包含只包含起源，象征含义，英文名组成解释！用80个汉字详细描述。(注意：这3个名字的寓意不能重复，必须使用不同的词语表达)
            发音：这个名字的音标和音节数。如果该发音和中文发音相似，请解释哪里相似！
                - 内容必须包含：音标和音节数。
                - 如果该发音和中文发音相似，请指出相似的那个英文和中文部分。
            人物形象：
                - 用大于100个字不超过270个字描述。
                - 四个名字的人物形象语言描述不能有重复描述，可以使用不同的词语或者语气，使回复看起来丰富多彩并且用非常自然的叙述方式。
                - 人物形象的描述不要出现星座的信息。
            流行度：这个名字在过去的流行度(注意：不包含欧美地区)。
            
            注意：你的最终回答除了JSON数组外，不要包含任何其他内容！
            </回答要求>
            """;

    private static final String USER_MESSAGE = "中文名字：%1。 性别：%2。星座或者MBTI：%3。期望寓意: %4。其他要求: %5。是否需要和中文名字发音相似：%6。";
    private static final String DPSK_MODEL = "deepseek-chat";
    private static final String QW_MODEl = "deepseek-r1";

    private static final Double TEMPERATURE = 1.9;
    private static final Logger log = LoggerFactory.getLogger(RestUtils.class);

    private static RestTemplate buildRestTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofMinutes(2L))
                .setReadTimeout(Duration.ofMinutes(2L)).build();
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


    public static String dpskChat(String msg, String token) throws JsonProcessingException {
        String uuid = UUID.randomUUID().toString();
        log.info("DP is answering: {}", uuid);
        String resp = postChat(msg, token, DPSK_MODEL, DPSK_CHAT_URL, uuid);
        if (resp == null) {
            log.error("Failed to chat with DP");
        } else {
            log.info("DP response: {} - {}", uuid, resp);
        }
        return resp;
    }


    public static String qwChat(String msg, String token) throws JsonProcessingException {
        String uuid = UUID.randomUUID().toString();
        log.info("QW is answering: {}", uuid);
        String resp = postChat(msg, token, QW_MODEl, QW_CHAT_URL, uuid);
        if (resp == null) {
            log.error("Failed to chat with qw");
        } else {
            log.info("QW response: {} - {}", uuid, resp);
        }
        return resp;
    }

    private static String postChat(String msg, String token, String model, String url, String uuid) throws JsonProcessingException {
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = getStringHttpEntity(uuid, model, headers, msg);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String respBody = response.getBody();
                return convertOpenAIJsonResponse(respBody);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("{}:{}", uuid, e.getMessage(), e);
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

    public static String jsonArrayToString(String resp) {
        if (resp == null || resp.isEmpty()) {
            return null;
        }
        if (resp.startsWith("```json") && resp.endsWith("```")) {
            resp = resp.replace("```json", "");
            resp = resp.substring(0, resp.length() - 3);
        }
        JSONArray jsonArray;
        try {
            jsonArray = JSON.parseArray(resp);
        } catch (Exception e) {
            log.error("{}", resp);
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            sb.append("- **名字**: ").append(jsonObject.getString("名字")).append("\n");
            sb.append("- **寓意**: ").append(jsonObject.getString("寓意")).append("\n");
            sb.append("- **发音**: ").append(jsonObject.getString("发音")).append("\n");
            sb.append("- **人物形象**: ").append(jsonObject.getString("人物形象")).append("\n");
            sb.append("- **流行度**: ").append(jsonObject.getString("流行度")).append("\n");
            sb.append("---\n");
        }
        return sb.toString();
    }

    public static String getUid(String code, String type) {
        String appId = "", appSecret = "";
        if (Constants.APP_TYPE_SUGAR.equals(type)) {
            appId = System.getenv(Constants.APP_ID);
            appSecret = System.getenv(Constants.APP_SECRET);
        } else if (Constants.APP_TYPE_SWEET.equals(type)) {
            appId = System.getenv(Constants.APP_ID_SWEET);
            appSecret = System.getenv(Constants.APP_SECRET_SWEET);
        }
        String url = Constants.UID_ENDPOINT.replace("${appid}", appId)
                .replace("${secret}", appSecret).replace("${code}", code);
        RestTemplate restTemplate = buildRestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject body = JSON.parseObject(response.getBody());
            assert body != null;
            return body.getString("openid");
        } catch (Exception e) {
            return "";
        }
    }

    public static String checkParams(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String name = jsonObject.getString("name");
        if (name == null || name.length() > Constants.MAX_LENGTH_20) {
            return "名字太长了，短一些试试~";
        }
        String mbti = jsonObject.getString("mbti");
        if (mbti == null || mbti.length() > Constants.MAX_LENGTH_20) {
            return "星座或者MBTI太长了，短一些试试~";
        }
        String meaning = jsonObject.getString("meaning");
        if (meaning == null || meaning.length() > Constants.MAX_LENGTH_20) {
            return "寓意太长了，短一些试试~";
        }
        String other = jsonObject.getString("other");
        if (other == null || other.length() > Constants.MAX_LENGTH_100) {
            return "其他要求太长了，短一些试试~";
        }
        return Constants.VERIFIED;
    }
}
