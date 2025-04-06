package evliess.io.config;

import evliess.io.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CustomFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public CustomFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith(Constants.PUBLIC_PATH_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(Constants.X_TOKEN);
        String openid = request.getHeader(Constants.X_OPENID);
        // Check if the openid is null or empty
        if (!TokenUtils.isValidOpenid(openid)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken("", "");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(openid, token);
        Authentication verifiedAuth = this.authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(verifiedAuth);
        filterChain.doFilter(request, response);
    }
}
