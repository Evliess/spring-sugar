package evliess.io.service;

import evliess.io.entity.AuditToken;
import evliess.io.jpa.AuditTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditTokenService {

    private static final Logger log = LoggerFactory.getLogger(AuditTokenService.class);

    private final AuditTokenRepository auditTokenRepository;

    @Autowired
    public AuditTokenService(AuditTokenRepository auditTokenRepository) {
        this.auditTokenRepository = auditTokenRepository;
    }

    private void saveAudit(String openid, String token, String type) {
        if (openid == null || openid.isEmpty() || token == null || token.isEmpty()) {
            return;
        }
        AuditToken auditToken = new AuditToken(openid, token, type);
        this.auditTokenRepository.save(auditToken);
    }

    public void saveAuditToken(String type) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String principal = authenticationToken.getPrincipal().toString();
            String[] info = principal.split("::");
            saveAudit(info[0], info[1], type);
        } catch (Exception e) {
            log.error("LLM save audit error!");
        }
    }

    public List<String> findUsersByToken() {
        return this.auditTokenRepository.findUsersByToken();
    }

    public List<AuditToken> findByUser(String user) {
        return this.auditTokenRepository.findByUser(user);
    }
}
