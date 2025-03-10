package evliess.io.config;

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
    private final String X_TOKEN = "X-token";

    public CustomFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/public/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(X_TOKEN);
        Authentication authentication = new UsernamePasswordAuthenticationToken(token, token);
        Authentication verifiedAuth = this.authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(verifiedAuth);
        filterChain.doFilter(request, response);
    }
}
