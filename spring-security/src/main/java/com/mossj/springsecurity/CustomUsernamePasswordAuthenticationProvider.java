package com.mossj.springsecurity;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    final private EmployeeRepo employeeRepo;
    private LoginAttemptService loginAttemptService;

    public CustomUsernamePasswordAuthenticationProvider(EmployeeRepo employeeRepo, LoginAttemptService loginAttemptService) {
        this.employeeRepo = employeeRepo;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        final WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        if (loginAttemptService.isBlocked(details.getRemoteAddress())) {
            throw new BadCredentialsException("Invalid ip");
        }

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Employee employee = employeeRepo.findByName(username)
            .orElseThrow(()-> new BadCredentialsException("Invalid username"));

        String usernameDB = employee.getName();
        String passwordDB = employee.getPassword();

        boolean isPasswordCorrect = BCrypt.checkpw(password, passwordDB);

        if (username.equals(usernameDB) && isPasswordCorrect) {
            return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
