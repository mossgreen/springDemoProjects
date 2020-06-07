package com.mossj.springsecurity;


import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

//@Component
public class AuthenticationLoginFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private LoginAttemptService loginAttemptService;

    public AuthenticationLoginFailureEventListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        loginAttemptService.loginFailed(details.getRemoteAddress());
    }
}
