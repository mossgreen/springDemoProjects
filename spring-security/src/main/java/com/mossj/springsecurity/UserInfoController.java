package com.mossj.springsecurity;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class UserInfoController {

    @GetMapping("/userInfo")
    public String userInfo(Authentication authentication) {
        return authentication.getName();
    }

    @GetMapping("/userInfo2")
    public String userInfo2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/userInfo3")
    public String userInfo3(Principal principal) {
        return principal.getName();
    }
}
