package com.ihobb.gm.security;

import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.service.OrganizationService;
import com.ihobb.gm.admin.service.OrganizationServiceImpl;
import com.ihobb.gm.admin.service.UserService;
import com.ihobb.gm.admin.service.UserServiceImpl;
import com.ihobb.gm.auth.domain.Authority;
import com.ihobb.gm.auth.domain.User;
import com.ihobb.gm.config.DBContextHolder;
import com.ihobb.gm.constant.JWTConstants;
import com.ihobb.gm.utility.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final OrganizationService organizationService;

    public JwtAuthenticationFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil, UserService userService, OrganizationService organizationService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.organizationService = organizationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("------------------");
        logger.info(request.getRequestURL().toString());


        String header = request.getHeader(JWTConstants.HEADER_STRING);
        String username = null;
        String orgcode = null;
        String authToken = null;

        if (null != header && header.startsWith(JWTConstants.TOKEN_PREFIX)) {
            authToken = header.replace(JWTConstants.TOKEN_PREFIX,"");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
                orgcode = jwtTokenUtil.getAudienceFromToken(authToken);
                Organization organization = organizationService.fetchOrganizationByCode(orgcode);
                if(null == organization){
                    logger.error("An error during getting org code");
                    throw new BadCredentialsException("Invalid org code.");
                }
                DBContextHolder.setCurrentDb(orgcode);
            } catch (IllegalArgumentException ex) {
                logger.error("An error during getting username from token", ex);
            } catch (ExpiredJwtException ex) {
                logger.warn("The token is expired and not valid anymore", ex);
            } catch(SignatureException ex){
                logger.error("Authentication Failed. Username or Password not valid.",ex);
            }
        } else {
            logger.warn("Couldn't find bearer string, will ignore the header");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {

                User user = userService.fetchUserByName(username);

                final Set<Authority> authorities = user.getAuthorities();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
