package com.mossj.springsecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        if (authenticationToken == null) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        var token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (!StringUtils.isEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {
                final Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SecurityConstants.JWT_SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

                final String username = claimsJws.getBody().getSubject();

                final List<SimpleGrantedAuthority> authorities = ((List<?>)claimsJws.getBody().get("rol")).stream()
                    .map(authority -> new SimpleGrantedAuthority((String) authority))
                    .collect(Collectors.toList());

                if (!StringUtils.isEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            } catch (ExpiredJwtException e) {
                log.warn("Request to parse expired JWT: {} failed: {}", token, e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("Request to parse unsupported JWT: {} failed: {}", token, e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("Request to parse invalid JWT: {} failed: {}", token, e.getMessage());
            } catch (SignatureException e) {
                log.warn("Request to parse JWT with invalid signature: {} faile: {}", token, e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("Request to parse empty or null JWT: {} failed: {}", token, e.getMessage());
            }
        }

        return null;
    }

}
