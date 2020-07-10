package com.ihobb.gm.admin.service;

import com.ihobb.gm.auth.domain.User;
import com.ihobb.gm.admin.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User fetchUserByEmail(String email) {
        return userRepository.findAllByEmailAndActivatedIsTrue(email).get(0);
    }

    @Override
    public User fetchUserByName(String username) {
        return userRepository.findAllByNameAndActivatedIsTrue(username).get(0);
    }
}
