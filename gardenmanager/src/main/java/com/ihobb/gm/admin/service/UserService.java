package com.ihobb.gm.admin.service;

import com.ihobb.gm.admin.domain.User;
import com.ihobb.gm.admin.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final List<User> users = userRepository.findAllByEmailAndActivatedIsTrue(username);
        if (users.isEmpty()) {
            throw new RuntimeException("Username or password are incorrect.");
        }
        return userRepository.findAllByEmailAndActivatedIsTrue(username).get(0);
    }
}
