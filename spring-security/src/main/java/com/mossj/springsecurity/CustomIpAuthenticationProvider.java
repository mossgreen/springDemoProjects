package com.mossj.springsecurity;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.ArrayList;
import java.util.List;

public class CustomIpAuthenticationProvider implements AuthenticationProvider {

    private final List<String> ipWhiteList = new ArrayList<>();

    public CustomIpAuthenticationProvider() {

        // this list can be populated from DB
        ipWhiteList.add("192.168.1.0");
        ipWhiteList.add("192.168.1.1");
        ipWhiteList.add("192.168.1.1");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String ip = details.getRemoteAddress();
        if (!ipWhiteList.contains(ip)) {
            throw new BadCredentialsException("Invalid Ip");
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
