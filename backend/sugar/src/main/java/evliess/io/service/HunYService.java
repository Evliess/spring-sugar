package evliess.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.interfaces.Inference;
import evliess.io.utils.RestUtils;
import org.springframework.stereotype.Service;


@Service
public class HunYService implements Inference {
    @Override
    public String chat(String message) throws JsonProcessingException {
        return RestUtils.hunYChat(message);
    }

    @Override
    public String chat(String message, String userPrompt, String systemPrompt) {
        return "null";
    }
}
