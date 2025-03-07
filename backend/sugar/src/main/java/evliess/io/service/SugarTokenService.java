package evliess.io.service;

import evliess.io.entity.SugarToken;
import evliess.io.jpa.SugarTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SugarTokenService {

    private final SugarTokenRepository sugarTokenRepository;

    @Autowired
    public SugarTokenService(SugarTokenRepository sugarTokenRepository) {
        this.sugarTokenRepository = sugarTokenRepository;
    }

    public void save(SugarToken token) {
        sugarTokenRepository.save(token);
    }
}
