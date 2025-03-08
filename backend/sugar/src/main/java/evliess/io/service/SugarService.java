package evliess.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import evliess.io.interfaces.Inference;
import evliess.io.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SugarService implements Inference {
    private static final Logger log = LoggerFactory.getLogger(SugarService.class);


    @Autowired
    private DpskService dpskService;

    @Autowired
    private QwService qwService;

    @Autowired
    private HunYService hunYService;

    @Autowired
    private OAService oaService;


    @Override
    public String chat(String message) throws JsonProcessingException {
        String response = dpskService.chat(message);
        if (response == null) {
            response = qwService.chat(message);
        }
        log.info(response);
        return response;
    }

    @Override
    public String chat(String message, String userPrompt, String systemPrompt) {
        return "null";
    }
}
