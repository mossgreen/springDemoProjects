package com.mossj.springsecurity;

import ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    final private PasswordEncoder passwordEncoder;

    public CustomUsernamePasswordAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        String usernameDB = "user";
//        String passwordDB = "123456";

        String passwordDB = passwordEncoder.encode("123456");

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
