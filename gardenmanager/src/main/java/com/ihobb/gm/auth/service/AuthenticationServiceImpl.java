package com.ihobb.gm.auth.service;

import com.ihobb.gm.admin.service.UserService;
import com.ihobb.gm.auth.domain.User;
import com.ihobb.gm.security.JwtUserDetailsService;
import com.ihobb.gm.utility.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationServiceImpl(UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String refreshToken(String token) {
        if(!jwtTokenUtil.isTokenExpired(token)){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public String login(String username, String password) {

        final User user = userService.fetchUserByName(username);

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtTokenUtil.generateToken(userDetails.getUsername(), String.valueOf(user.getCurrentOrgCode()));
    }
}
