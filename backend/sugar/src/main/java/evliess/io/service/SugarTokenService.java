package evliess.io.service;

import evliess.io.entity.SugarToken;
import evliess.io.jpa.SugarTokenRepository;
import evliess.io.utils.TokenUtils;
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

    public boolean isTokenValid(String token) {
        SugarToken inactiveToken = sugarTokenRepository.findByToken(token + "inactive");
        return TokenUtils.isValidToken(token) && null == inactiveToken;
    }
}
