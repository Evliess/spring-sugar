package evliess.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.interfaces.Inference;
import evliess.io.utils.RestUtils;
import org.springframework.stereotype.Service;

@Service
public class DpskService implements Inference {
    @Override
    public String chat(String message) throws JsonProcessingException {
        return RestUtils.dpskChat(message);
    }

    @Override
    public String chat(String message, String userPrompt, String systemPrompt) {
        return "null";
    }
}
