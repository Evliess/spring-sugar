package evliess.io.config;

import evliess.io.jpa.SugarUserRepository;
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication != null) {
            if (authentication.getPrincipal() == null) {
                return null;
            }
            String principal = authentication.getPrincipal().toString();
            if (sugarUserRepository.findByUsername(principal) != null) {
                return new UsernamePasswordAuthenticationToken(principal + Constants.DOUBLE_COLON + principal, principal, List.of());
            }

            if (authentication.getCredentials() == null) {
                return null;
            }
            String credentials = authentication.getCredentials().toString();
            if (TokenUtils.isValidToken(credentials) && sugarUserService.findByToken(credentials) != null) {
                List<SimpleGrantedAuthority> roles = List.of();
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
