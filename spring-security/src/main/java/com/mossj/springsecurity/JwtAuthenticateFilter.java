package com.mossj.springsecurity;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Log4j2
//@Component
public class JwtAuthenticateFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final String strKey = "pRNsm7m8JoGl0q9uY8F/YquCcCA4rFPzjZqtMdzrqPk=";

    public JwtAuthenticateFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/token");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        return this.authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final User user = (User) authResult.getPrincipal();
        final List<String> roles = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        SecretKey key = Keys.hmacShaKeyFor(strKey.getBytes());
        final String token = Jwts.builder()
            .setHeaderParam("TYP", "JWT")
            .setIssuer("moss.example")
            .setAudience("you")
            .setExpiration(new Date(System.currentTimeMillis() + 1000000))
            .setSubject(user.getUsername())
            .setIssuedAt(new Date())
            .setIssuer("www.moss.example.com")
            .setSubject(user.getUsername())
            .signWith(key)
            .compact();

        response.setHeader("Authorization", "Bearer " + token);
    }
}
