package evliess.io.config;

import evliess.io.utils.TokenUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication != null) {
            String principal = authentication.getPrincipal().toString();
            if (TokenUtils.isValidToken(principal)) {
                List<SimpleGrantedAuthority> roles = List.of();
                return new UsernamePasswordAuthenticationToken("", "", roles);
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
