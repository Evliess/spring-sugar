package evliess.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.interfaces.Inference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SugarService implements Inference {

    @Autowired
    private DpskService service;

    @Autowired
    private QwService qwService;


    @Override
    public String chat(String message) throws JsonProcessingException {
        String response = service.chat(message);
        if (response == null) {
            response = qwService.chat(message);
        }
        return response;
    }

    @Override
    public String chat(String message, String userPrompt, String systemPrompt) {
        return "null";
    }
}
