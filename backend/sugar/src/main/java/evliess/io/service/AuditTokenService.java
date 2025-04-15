package evliess.io.service;

import evliess.io.entity.AuditToken;
import evliess.io.jpa.AuditTokenRepository;
import evliess.io.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
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
            log.info("Save audit success! user: {}, type: {}", info[0], type);
        } catch (Exception e) {
            log.error("LLM save audit error!");
        }
    }

    public void saveAuditToken(String openid, String type) {
        saveAudit(openid, "free", type);
        log.info("Save audit success! user: {}, type: {}", openid, type);
    }

    public List<String> findUsersByToken() {
        return this.auditTokenRepository.findUsersByToken();
    }

    public List<AuditToken> findByUser(String user) {
        return this.auditTokenRepository.findByUser(user);
    }


    public List<AuditToken> findLast24H() {
        Long end = Instant.now().toEpochMilli();
        Long start = Instant.now().minus(Duration.ofDays(1)).toEpochMilli();
        return this.auditTokenRepository.findByTimeSpan(start, end);
    }

    public List<AuditToken> findLast3D() {
        Long end = Instant.now().toEpochMilli();
        Long start = Instant.now().minus(Duration.ofDays(3)).toEpochMilli();
        return this.auditTokenRepository.findByTimeSpan(start, end);
    }

    public List<AuditToken> findLast7D() {
        Long end = Instant.now().toEpochMilli();
        Long start = Instant.now().minus(Duration.ofDays(7)).toEpochMilli();
        return this.auditTokenRepository.findByTimeSpan(start, end);
    }

    public List<AuditToken> findLast30D() {
        Long end = Instant.now().toEpochMilli();
        Long start = Instant.now().minus(Duration.ofDays(30)).toEpochMilli();
        return this.auditTokenRepository.findByTimeSpan(start, end);
    }

    public AuditToken findLatestValidToken(String user) {
        AuditToken auditToken = auditTokenRepository.findLatestTokenByUser(user);
        if (auditToken == null) {
            return null;
        } else {
            if (TokenUtils.isValidToken(auditToken.getToken())) {
                return auditToken;
            } else {
                return null;
            }
        }
    }
}
