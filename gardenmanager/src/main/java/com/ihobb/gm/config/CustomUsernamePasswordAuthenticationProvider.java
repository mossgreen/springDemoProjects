package com.ihobb.gm.config;

import com.ihobb.gm.admin.domain.User;
import com.ihobb.gm.admin.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Component
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    public CustomUsernamePasswordAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userEmail = authentication.getName();
        String password = authentication.getCredentials().toString();

        final List<User> users = userRepository.findAllByEmailAndActivatedIsTrue(userEmail);

        if (users.isEmpty()) {
            throw new RuntimeException("failed authentication");
        } else {
            final User theUser = users.stream().filter(u -> u.getEmail().equals(userEmail)).findAny().orElseThrow(RuntimeException::new);

//            boolean isPasswordCorrect = BCrypt.checkpw(password, passwordDB);

            if (theUser.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(userEmail, password, theUser.getAuthorities());
            } else {
                throw new RuntimeException("failed authentication");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
