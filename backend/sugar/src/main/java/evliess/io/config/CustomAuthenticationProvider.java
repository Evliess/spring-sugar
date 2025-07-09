package evliess.io.config;

import evliess.io.entity.AuditToken;
import evliess.io.jpa.AuditTokenRepository;
import evliess.io.jpa.SugarUserRepository;
import evliess.io.service.SugarTokenService;
import evliess.io.service.SugarUserService;
import evliess.io.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private SugarUserService sugarUserService;

    @Autowired
    private SugarUserRepository sugarUserRepository;

    @Autowired
    private SugarTokenService sugarTokenService;

    @Autowired
    private AuditTokenRepository auditTokenRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication != null) {
            if (authentication.getPrincipal() == null) {
                return authentication;
            }
            String principal = authentication.getPrincipal().toString();
            if (sugarUserRepository.findByUsername(principal) != null) {
                return new UsernamePasswordAuthenticationToken(principal + Constants.DOUBLE_COLON + principal, principal, List.of());
            }

            if (authentication.getCredentials() == null) {
                return authentication;
            }
            String credentials = authentication.getCredentials().toString();
            if (sugarTokenService.isTokenValid(credentials) && TokenUtils.isValidOpenid(principal)) {
                List<SimpleGrantedAuthority> roles = List.of();
                AuditToken auditToken = new AuditToken(principal, credentials, Constants.TYPE_LLM);
                this.auditTokenRepository.save(auditToken);
                return new UsernamePasswordAuthenticationToken(principal + Constants.DOUBLE_COLON + credentials, credentials, roles);
            } else {
                return authentication;
            }
        }
        return null;
    }


    // which type of authentication will be verified
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
