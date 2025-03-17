package evliess.io.service;

import evliess.io.entity.AuditToken;
import evliess.io.jpa.AuditTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditTokenService {
    private final AuditTokenRepository auditTokenRepository;

    @Autowired
    public AuditTokenService(AuditTokenRepository auditTokenRepository) {
        this.auditTokenRepository = auditTokenRepository;
    }

    public void saveAudit(String openid, String token) {
        if (openid == null || openid.isEmpty() || token == null || token.isEmpty()) {
            return;
        }
        AuditToken auditToken = new AuditToken(openid, token);
        this.auditTokenRepository.save(auditToken);
    }

    public List<String> findUsersByToken() {
        return this.auditTokenRepository.findUsersByToken();
    }

    public List<AuditToken> findByUser(String user) {
        return this.auditTokenRepository.findByUser(user);
    }
}
