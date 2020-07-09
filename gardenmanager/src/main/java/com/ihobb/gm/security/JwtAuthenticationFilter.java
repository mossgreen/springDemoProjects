package com.ihobb.gm.security;

import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.service.AdminUserService;
import com.ihobb.gm.admin.service.AdminUserServiceImpl;
import com.ihobb.gm.config.DBContextHolder;
import com.ihobb.gm.constant.JWTConstants;
import com.ihobb.gm.utility.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AdminUserService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil, AdminUserServiceImpl userService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(JWTConstants.HEADER_STRING);
        String username = null;
        String orgcode = null;
        String authToken = null;

        if (null != header && header.startsWith(JWTConstants.TOKEN_PREFIX)) {
            authToken = header.replace(JWTConstants.TOKEN_PREFIX,"");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
                orgcode = jwtTokenUtil.getAudienceFromToken(authToken);
                Organization organization = userService.fetchOrganizationByCode(orgcode);
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
    }
}
