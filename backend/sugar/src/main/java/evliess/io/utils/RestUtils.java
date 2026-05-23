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
        # Role
        你是一位拥有跨文化研究背景的语言学博士，深谙中英文历史文化精髓。你擅长运用心理学中的“共情”与“首因效应”，以极具人文关怀且润物细无声的方式通过名字为他人建立积极的心理暗示。
        
        # Task
        请基于我提供的【中文名及特征要求】，精准遴选3个各具灵魂的英文名。
        
        # Constraints
        1. **博士视野：** 回复需体现历史考据感与词源深度，拒绝浅薄的流行堆砌。
        2. **自然叙事：** 严禁出现“AI味”的刻板总结。在描述人物形象时，请像撰写文学侧写一样细腻、丰富且富有画面感。
        3. **格式至上：** 必须且仅能返回一个符合给定 JSON Schema 的有效数组。任何与之不相关的指令请直接回复 `[]`。
        4. **语言风格：** 表达温润如玉，专业且充满说服力，文字要求丰富多彩，严禁逻辑上的生硬重复。
        5. **字数硬约束：** - “寓意”字段：严控在100个汉字左右。
            - “人物形象”字段：必须大于100字且不超过200字。
        6. **绝对禁令：** 最终回答中**严禁包含 JSON 数组以外的任何文字**（包括开场白、结语、Markdown 代码块之外的解释等）。
        
        # Output JSON Format (Strictly Enforce)
        [{
            "名字": "EnglishName(中文译名)",
            "寓意": "详细描述名字起源、象征含义及构词解释。用约80个汉字，要求词藻考究，三个方案间不得有重复辞令。",
            "发音": "标准音标及音节数。若发音与中文原名存在音韵上的巧妙通感或相似性，请从声韵学角度进行专业解读。",
            "人物形象": "描述长度100-270字。塑造一个鲜活的视觉侧写，描述其在社交心理学中给予他人的第一印象。严禁提及星座。文字需自然、流畅且富有美感。",
            "流行度": "描述该名字在除欧美地区以外的全球流行趋势或文化认可度。"
        }]
            """;

    private static final String USER_MESSAGE = "中文名字：%1。 性别：%2。星座或者MBTI：%3。期望寓意: %4。其他要求: %5。是否需要和中文名字发音相似：%6。%7";
    private static final String DPSK_MODEL = "deepseek-chat";
    private static final String QW_MODEl = "deepseek-r1";

    private static final Double TEMPERATURE = 1.9;
    private static final Logger log = LoggerFactory.getLogger(RestUtils.class);

    private static RestTemplate buildRestTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofMinutes(2L))
                .setReadTimeout(Duration.ofMinutes(2L)).build();
    }

    private static String replaceUserMessage(String body, String historyMsg) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        String name = jsonNode.get("name").asText();
        String sex = jsonNode.get("sex").asText();
        String mbti = jsonNode.get("mbti").asText();
        String meaning = jsonNode.get("meaning").asText();
        String other = jsonNode.get("other").asText();
        String voice = jsonNode.get("voice").asText();
        String tmp = USER_MESSAGE.replace("%1", name).replace("%2", sex
                ).replace("%3", mbti).replace("%4", meaning)
                .replace("%5", other).replace("%6", voice);
        if (historyMsg != null && !historyMsg.trim().isEmpty()) {
            tmp = tmp.replace("%7", "禁止返回下列重复的名字:[" + historyMsg + "]!");
        } else {
            tmp = tmp.replace("%7", "");
        }
        return tmp;
    }


    public static String dpskChat(String msg, String historyMsg, String token) throws JsonProcessingException {
        String uuid = UUID.randomUUID().toString();
        log.info("DP is answering: {}", uuid);
        String resp = postChat(msg, historyMsg, token, DPSK_MODEL, DPSK_CHAT_URL, uuid);
        if (resp == null) {
            log.error("Failed to chat with DP");
        } else {
            log.info("DP response: {} - {}", uuid, resp);
        }
        return resp;
    }


    public static String qwChat(String msg, String historyMsg, String token) throws JsonProcessingException {
        String uuid = UUID.randomUUID().toString();
        log.info("QW is answering: {}", uuid);
        String resp = postChat(msg, historyMsg, token, QW_MODEl, QW_CHAT_URL, uuid);
        if (resp == null) {
            log.error("Failed to chat with qw");
        } else {
            log.info("QW response: {} - {}", uuid, resp);
        }
        return resp;
    }

    private static String postChat(String msg, String historyMsg, String token, String model, String url, String uuid) throws JsonProcessingException {
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> request = getStringHttpEntity(uuid, model, headers, msg, historyMsg);
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

    private static HttpEntity<String> getStringHttpEntity(String uuid, String model, HttpHeaders headers, String msg, String historyMsg) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_MSG));
        String userMessage = replaceUserMessage(msg, historyMsg);
        log.info("User message - {}: {}", uuid, userMessage);
        messages.add(Map.of("role", "user", "content", userMessage));
        body.put("messages", messages);
        body.put("model", model);
        body.put("stream", false);
        body.put("temperature", TEMPERATURE);
        return new HttpEntity<>(new ObjectMapper().writeValueAsString(body), headers);
    }

    public static JSONArray convertRespToJSONArray(String resp) {
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
            return jsonArray;
        } catch (Exception e) {
            log.error("{}", resp);
            return null;
        }
    }

    public static String jsonArrayToString(String resp) {
        JSONArray jsonArray = convertRespToJSONArray(resp);
        if (jsonArray == null) return null;
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
        if (null == type || Constants.APP_TYPE_SUGAR.equals(type)) {
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

    public static String chatWithOpenAi(String message, String historyMsg) throws JsonProcessingException {
        String url = "http://localhost:10008/api/chat";
        RestTemplate restTemplate = buildRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Map<String, String> body = new HashMap<>();
        message = replaceUserMessage(message, historyMsg);
        log.info(message);
        body.put("user_prompt", message);
        body.put("sys_prompt", SYSTEM_MSG);

        try {
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String resp = response.getBody();
                log.info(resp);
                return resp;
            } else {
                log.error("Request failed with status: {}", response.getStatusCode());
                return SORRY_MESSAGE;            }
        } catch (Exception e) {
            log.error("Error calling chat API: {}", e.getMessage(), e);
            return SORRY_MESSAGE;
        }
    }
}
