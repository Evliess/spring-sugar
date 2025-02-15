package evliess.io.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Inference {

    String chat(String message) throws JsonProcessingException;
    String chat(String message, String userPrompt, String systemPrompt);
}
