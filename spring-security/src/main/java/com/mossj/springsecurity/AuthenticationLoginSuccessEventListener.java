package com.mossj.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationLoginSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private LoginAttemptService loginAttemptService;

    public AuthenticationLoginSuccessEventListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        loginAttemptService.loginSucceed(webAuthenticationDetails.getRemoteAddress()
        );

    }
}
