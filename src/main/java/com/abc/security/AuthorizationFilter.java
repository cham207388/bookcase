package com.abc.security;

import com.abc.exception.AuthorNotFoundException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.abc.security.SecurityConstants.*;
@Slf4j
/**
 * When user is authenticated, this class is triggered: Authorization
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {
    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String headerToken = req.getHeader(getHeaderString());
        String headerEmail = req.getHeader(getHeaderEmail());

        if (headerToken == null || !headerToken.startsWith(getTokenPrefix()) ||
                headerEmail.isEmpty() || !headerEmail.equals(getLoggingEmail())) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String token = req.getHeader(getHeaderString());
        if (token != null) {
            token = token.substring(getTokenPrefix().length());
            String user = Jwts.parser()
                    .setSigningKey(getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(
                        user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
