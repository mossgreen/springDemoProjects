package com.ihobb.gm.security;

import com.ihobb.gm.auth.domain.User;
import com.ihobb.gm.admin.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final List<User> users = userRepository.findAllByNameAndActivatedIsTrue(username);

        if (users.isEmpty()) {
            log.info("Failed to fetch user by name: {}, no result.", username);
            throw new UsernameNotFoundException("Invalid user name or password.");
        } else {
            if (users.size() != 1) {
                log.info("Failed to fetch user by name: {}, multiple users found.", username);
                throw new UsernameNotFoundException("Invalid user name or password.");
            } else {
                User user = users.get(0);
                return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), user.getAuthorities());
            }
        }
    }
}
